package com.ssf.eligiblecouple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
import com.ssf.eligiblecouple.dtos.*;
import com.ssf.eligiblecouple.entities.EligibleCouple;
import com.ssf.eligiblecouple.exception.EntityNotFoundException;
import com.ssf.eligiblecouple.kafka.KafkaProducer;
import com.ssf.eligiblecouple.kafka.message.Actor;
import com.ssf.eligiblecouple.kafka.message.Context;
import com.ssf.eligiblecouple.kafka.message.KafkaMessage;
import com.ssf.eligiblecouple.kafka.topic.Topics;
import com.ssf.eligiblecouple.repository.IEligibleCoupleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class EligibleCoupleService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private IEligibleCoupleRepository repository;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Validator validator;
    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;

    public EligibleCoupleDto createEc(EligibleCoupleDto eligibleCoupleDto) {
        log.debug("Creating the EC for rchid {}", eligibleCoupleDto.getRchId());
        EligibleCouple eligibleCouple = mapper.map(eligibleCoupleDto, EligibleCouple.class);
        EligibleCouple resultEntity = repository.save(eligibleCouple);
        EligibleCoupleDto eligibleCoupleDtoResult = mapper.map(resultEntity, EligibleCoupleDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(eligibleCouple);
        eligibleCoupleDtoResult.setAudit(auditDto);

        if (kafkaEnabled) {
            KafkaMessage<EligibleCoupleDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(EligibleCoupleConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(EligibleCoupleConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ECService + "Created");
            kafkaMessage.setObject(eligibleCoupleDtoResult);
            kafkaProducer.sendMessage(Topics.ECService.toString(), kafkaMessage);
        }

        return eligibleCoupleDtoResult;
    }

    public EligibleCoupleDto patchEc(String serviceId, Map<String, Object> eligibleCoupleDto) {
        log.debug("Updating the EC for serviceId {}", serviceId);
        EligibleCoupleDto coupleDto = objectMapper.convertValue(eligibleCoupleDto, EligibleCoupleDto.class);
        Set<ConstraintViolation<EligibleCoupleDto>> violations = validator.validate(coupleDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid request", violations);
        }

        EligibleCouple existingEc = getEligibleCouple(serviceId);
        // copy the fields which are changed to entity object
        Map<String, Object> dbMap = objectMapper.convertValue(existingEc, Map.class);
        List<String> oldPcServices = new ArrayList<>();

        if (eligibleCoupleDto.containsKey("pcServiceList")) {
            List<String> newPcServices = (List<String>) eligibleCoupleDto.get("pcServiceList");
            if (dbMap.containsKey("pcServiceList") && dbMap.get("pcServiceList") != null) {
                oldPcServices = (List<String>) dbMap.get("pcServiceList");
            }
            oldPcServices.addAll(newPcServices);
            eligibleCoupleDto.put("pcServiceList", oldPcServices);
        }
        if (eligibleCoupleDto.containsKey("couple")) {
            eligibleCoupleDto.put("couple", eligibleCoupleDto.get("couple"));
        }
        dbMap.putAll(eligibleCoupleDto);
        EligibleCouple data = objectMapper.convertValue(dbMap, EligibleCouple.class);
        repository.save(data);

        //response generate
        EligibleCoupleDto response = mapper.map(data, EligibleCoupleDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(existingEc);
        response.setAudit(auditDto);

        if (kafkaEnabled) {
            KafkaMessage<EligibleCoupleDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(EligibleCoupleConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(EligibleCoupleConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ECService + "Updated");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.ECService.toString(), kafkaMessage);
        }

        return response;

    }

    public EligibleCoupleDto readEc(String serviceId) {
        log.debug("Reading the EC for serviceId {}", serviceId);
        auditorProvider.getCurrentAuditor();
        EligibleCouple eligibleCouple = getEligibleCouple(serviceId);
        EligibleCoupleDto eligibleCoupleDto = mapper.map(eligibleCouple, EligibleCoupleDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(eligibleCouple);
        eligibleCoupleDto.setAudit(auditDto);

        return eligibleCoupleDto;
    }

    public EligibleCouplePageResponse filterEc(String rchId, String serviceId, String dataEntryStatus, int page, int size) {
        log.debug("Filtering  the EC for rchId {} ,serviceId {}", rchId, serviceId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        EligibleCouple example = EligibleCouple.builder().build();
        Page<EligibleCouple> eligibleCouples;
        if (StringUtils.isNotBlank(rchId)) {
            example.setRchId(rchId);
        }
        if (StringUtils.isNotBlank(serviceId)) {
            example.setId(serviceId);
        }
        if (StringUtils.isNotBlank(dataEntryStatus)) {
            example.setDataEntryStatus(dataEntryStatus);
        }
        eligibleCouples = repository.findAll(Example.of(example), paging);
        return buildEligibleCouplePageResponse(eligibleCouples);

    }

    private EligibleCouplePageResponse buildEligibleCouplePageResponse(Page<EligibleCouple> eligibleCouples) {
        if (eligibleCouples == null || eligibleCouples.isEmpty()) {
            log.debug(" Pagination result is null ");
            return EligibleCouplePageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned ");
            Page<EligibleCoupleDto> eligibleCoupleDtoPage = buildEligibleCoupleDtos(eligibleCouples);
            return EligibleCouplePageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages(Long.valueOf(eligibleCouples.getTotalPages()))
                            .totalElements(Long.valueOf(eligibleCouples.getTotalElements()))
                            .number(eligibleCouples.getNumber())
                            .size(eligibleCouples.getSize())
                            .build())
                    .data(eligibleCoupleDtoPage.toList())
                    .build();
        }
    }

    private Page<EligibleCoupleDto> buildEligibleCoupleDtos(Page<EligibleCouple> eligibleCouples) {
        return eligibleCouples.map(eligibleCouple -> {
            // Conversion logic
            EligibleCoupleDto eligibleCoupleDto = mapper.map(eligibleCouple, EligibleCoupleDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(eligibleCouple);
            eligibleCoupleDto.setAudit(auditDto);
            return eligibleCoupleDto;
        });
    }

    public EligibleCouple getEligibleCouple(String serviceId) {
        return repository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("serviceId ::" + serviceId + " not found"));
    }

    private AuditDto buildAuditDto(EligibleCouple eligibleCouple) {
        return AuditDto.builder().createdBy(eligibleCouple.getCreatedBy())
                .modifiedBy(eligibleCouple.getModifiedBy())
                .dateCreated(eligibleCouple.getDateCreated())
                .dateModified(eligibleCouple.getDateModified())
                .build();
    }

}
