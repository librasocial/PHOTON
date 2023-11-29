package com.ssf.eligiblecouple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
import com.ssf.eligiblecouple.dtos.*;
import com.ssf.eligiblecouple.entities.VisitLog;
import com.ssf.eligiblecouple.exception.EntityNotFoundException;
import com.ssf.eligiblecouple.exception.MissmatchException;
import com.ssf.eligiblecouple.kafka.KafkaProducer;
import com.ssf.eligiblecouple.kafka.message.Actor;
import com.ssf.eligiblecouple.kafka.message.Context;
import com.ssf.eligiblecouple.kafka.message.KafkaMessage;
import com.ssf.eligiblecouple.kafka.topic.Topics;
import com.ssf.eligiblecouple.repository.IVisitLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class VisitLogService {

    @Autowired
    private EligibleCoupleService coupleService;
    @Autowired
    private IVisitLogRepository repository;
    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;

    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private KafkaProducer kafkaProducer;

    public VisitLogDto createVisitLog(String serviceId, VisitLogDto request) {
        log.debug("Creating the Visitlog for serviceId {}", serviceId);

        //validate
        EligibleCoupleDto eligibleCoupleDto = mapper.map(coupleService.getEligibleCouple(serviceId), EligibleCoupleDto.class);
        if (!serviceId.equals(request.getServiceId())) {
            throw new MissmatchException("serviceId " + serviceId + " missmatch with request service id " + request.getServiceId());
        }
        VisitLog visitLog = mapper.map(request, VisitLog.class);
        repository.save(visitLog);
        VisitLogDto response = mapper.map(visitLog, VisitLogDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        response.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<VisitLogMessagePayload> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(EligibleCoupleConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(EligibleCoupleConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ECVisitLog + "Created");
            kafkaMessage.setObject(VisitLogMessagePayload.builder().visitLogDto(response).eligibleCoupleDto(eligibleCoupleDto).build());
            kafkaProducer.sendMessage(Topics.ECVisitLog.toString(), kafkaMessage);
        }
        return response;
    }

    public VisitLogDto patchVisitLog(String serviceId, String logId, Map<String, Object> request) {
        log.debug("Update the Visitlog for serviceId {} and logId ", serviceId, logId);

        EligibleCoupleDto eligibleCoupleDto = mapper.map(coupleService.getEligibleCouple(serviceId), EligibleCoupleDto.class);
        var visitLog = getVisitLog(logId, serviceId);
        var visitLogDbMap = objectMapper.convertValue(visitLog, Map.class);
        visitLogDbMap.putAll(request);
        var visitLogEntity = objectMapper.convertValue(visitLogDbMap, VisitLog.class);
        repository.save(visitLogEntity);

        VisitLogDto response = mapper.map(visitLogEntity, VisitLogDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        response.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<VisitLogMessagePayload> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(EligibleCoupleConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(EligibleCoupleConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ECVisitLog + "Updated");
            kafkaMessage.setObject(VisitLogMessagePayload.builder().visitLogDto(response).eligibleCoupleDto(eligibleCoupleDto).build());
            kafkaProducer.sendMessage(Topics.ECVisitLog.toString(), kafkaMessage);
        }
        return response;

    }

    public VisitLogDto readVisitLog(String serviceId, String logId) {
        log.debug("Read the Visitlog for serviceId {} and logId ", serviceId, logId);
        auditorProvider.getCurrentAuditor();
        VisitLog visitLog = getVisitLog(logId, serviceId);
        VisitLogDto visitLogDto = mapper.map(visitLog, VisitLogDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        visitLogDto.setAudit(auditDto);

        return visitLogDto;

    }

    public VisitLogDtoPageResponse filterVisitLog(String serviceId, String logId, String rchId, int page, int size) {
        log.debug("filter the Visitlog for serviceId {} , logId {}  rchId {}  ", serviceId, logId, rchId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        VisitLog example = VisitLog.builder().build();
        if (StringUtils.isNotBlank(serviceId)) {
            example.setServiceId(serviceId);
        }
        if (StringUtils.isNotBlank(logId)) {
            example.setId(logId);
        }
        if (StringUtils.isNotBlank(rchId)) {
            example.setRchId(rchId);
        }
        return buildVisitLogPageResponse(repository.findAll(Example.of(example), paging));
    }

    public VisitLog getVisitLog(String logId, String serviceId) {
        return repository.findByIdAndServiceId(logId, serviceId)
                .orElseThrow(() -> new EntityNotFoundException("logId :: serviceId " + logId + " :: " + serviceId + " not found"));
    }

    private AuditDto buildAuditDto(VisitLog visitLog) {
        return AuditDto.builder().createdBy(visitLog.getCreatedBy())
                .modifiedBy(visitLog.getModifiedBy())
                .dateCreated(visitLog.getDateCreated())
                .dateModified(visitLog.getDateModified())
                .build();
    }

    private Page<VisitLogDto> buildVisitLogDtos(Page<VisitLog> visitLogs) {
        return visitLogs.map(v -> {
            // Conversion logic
            VisitLogDto visitLogDto = mapper.map(v, VisitLogDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(v);
            visitLogDto.setAudit(auditDto);
            return visitLogDto;
        });
    }

    private VisitLogDtoPageResponse buildVisitLogPageResponse(Page<VisitLog> visitLogs) {
        if (visitLogs == null || visitLogs.isEmpty()) {
            log.debug(" Pagination result is null ");
            return VisitLogDtoPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + visitLogs.getTotalElements());
            Page<VisitLogDto> visitLogDtos = buildVisitLogDtos(visitLogs);
            return VisitLogDtoPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages(Long.valueOf(visitLogs.getTotalPages()))
                            .totalElements(Long.valueOf(visitLogs.getTotalElements()))
                            .number(visitLogs.getNumber())
                            .size(visitLogs.getSize())
                            .build())
                    .data(visitLogDtos.toList())
                    .build();
        }
    }
}
