package com.ssf.ancservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancservice.constant.ANCServiceConst;
import com.ssf.ancservice.dtos.*;
import com.ssf.ancservice.entities.VisitLog;
import com.ssf.ancservice.exception.EntityNotFoundException;
import com.ssf.ancservice.exception.MissmatchException;
import com.ssf.ancservice.kafka.KafkaProducer;
import com.ssf.ancservice.kafka.message.Actor;
import com.ssf.ancservice.kafka.message.Context;
import com.ssf.ancservice.kafka.message.KafkaMessage;
import com.ssf.ancservice.kafka.topic.Topics;
import com.ssf.ancservice.repository.IVisitLogRepository;
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
    public AuditorAware<String> auditorProvider;
    @Autowired
    private ANCServiceService coupleService;
    @Autowired
    private IVisitLogRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;


    public VisitLogDto createVisitLog(String serviceId, VisitLogDto visitLogDto) {
        log.debug("Creating the Visitlog for serviceId {}", serviceId);

        //validate
        ANCServiceDto ancServiceDto = mapper.map(coupleService.getANCService(serviceId), ANCServiceDto.class);
        if (!serviceId.equals(visitLogDto.getServiceId())) {
            throw new MissmatchException("serviceId " + serviceId + " missmatch with request service id " + visitLogDto.getServiceId());
        }
        VisitLog visitLog = mapper.map(visitLogDto, VisitLog.class);
        repository.save(visitLog);
        VisitLogDto response = mapper.map(visitLog, VisitLogDto.class);
        if (kafkaEnabled) {
            KafkaMessage<VisitLogMessagePayload> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ANCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ANCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ANCVisitLog + "Created");
            kafkaMessage.setObject(VisitLogMessagePayload.builder().visitLogDto(visitLogDto).ancServiceDto(ancServiceDto).build());
            kafkaProducer.sendMessage(Topics.ANCVisitLog.toString(), kafkaMessage);
        }

        // Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        response.setAudit(auditDto);
        return response;
    }

    public VisitLogDto patchVisitLog(String serviceId, String logId, Map<String, Object> patchDto) {
        log.debug("Update the Visitlog for serviceId {} and logId ", serviceId, logId);
        //validate
        ANCServiceDto ancServiceDto = mapper.map(coupleService.getANCService(serviceId), ANCServiceDto.class);
        var visitLog = getVisitLog(logId, serviceId);
        var visitLogDbMap = objectMapper.convertValue(visitLog, Map.class);
        visitLogDbMap.putAll(patchDto);
        var visitLogEntity = objectMapper.convertValue(visitLogDbMap, VisitLog.class);
        repository.save(visitLogEntity);

        VisitLogDto response = mapper.map(visitLogEntity, VisitLogDto.class);
        if (kafkaEnabled) {
            KafkaMessage<VisitLogMessagePayload> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ANCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ANCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ANCVisitLog + "Updated");
            kafkaMessage.setObject(VisitLogMessagePayload.builder().visitLogDto(response).ancServiceDto(ancServiceDto).build());
            kafkaProducer.sendMessage(Topics.ANCVisitLog.toString(), kafkaMessage);
        }
        // Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        response.setAudit(auditDto);
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
