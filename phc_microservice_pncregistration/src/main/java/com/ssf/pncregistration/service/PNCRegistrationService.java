package com.ssf.pncregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import com.ssf.pncregistration.dtos.*;
import com.ssf.pncregistration.entities.Couple;
import com.ssf.pncregistration.entities.DeliveryDetails;
import com.ssf.pncregistration.entities.MensuralPeriod;
import com.ssf.pncregistration.entities.PNCRegistration;
import com.ssf.pncregistration.exception.EntityNotFoundException;
import com.ssf.pncregistration.kafka.producer.client.KafkaProducerClient;
import com.ssf.pncregistration.kafka.producer.model.Actor;
import com.ssf.pncregistration.kafka.producer.model.Context;
import com.ssf.pncregistration.kafka.producer.model.KafkaMessage;
import com.ssf.pncregistration.kafka.topics.Topics;
import com.ssf.pncregistration.repository.IPNCRegistrationRepository;
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
public class PNCRegistrationService {

    @Autowired
    public AuditorAware<String> auditorProvider;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private IPNCRegistrationRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProducerClient producerClient;
    @Value("${kafka.enabled}")
    private Boolean kafkaEnable;

    public PNCRegistrationDto registration(PNCRegistrationDto registrationDto) {
        log.debug("Registration for PNC ");
        PNCRegistration registration = mapper.map(registrationDto, PNCRegistration.class);
        repository.save(registration);
        PNCRegistrationDto pncRegistrationDto = mapper.map(registration, PNCRegistrationDto.class);
        if (kafkaEnable) {
            KafkaMessage<PNCRegistrationDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCRegistration + "Created");
            kafkaMessage.setObject(pncRegistrationDto);
            producerClient.publishToTopic(Topics.PNCRegistration.toString(), kafkaMessage);
        }
        // Audit save
        AuditDto auditDto = buildAuditDto(registration);
        pncRegistrationDto.setAudit(auditDto);

        return pncRegistrationDto;
    }

    public PNCRegistrationDto readPNC(String registrationId) {
        log.debug("Reading the PNC for registrationId {}", registrationId);
        auditorProvider.getCurrentAuditor();
        PNCRegistration registration = getPNCReg(registrationId);
        PNCRegistrationDto registrationDto = mapper.map(registration, PNCRegistrationDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(registration);
        registrationDto.setAudit(auditDto);

        return registrationDto;
    }

    public PNCRegistrationDto patchPNC(String registrationId, PNCRegPatchDto pncRequest) {
        log.debug("Updating the PNC for registrationId {}", registrationId);
        var registration = getPNCReg(registrationId);
        var inputMap = pncRequest.getProperties();
        switch (pncRequest.getType()) {
            case PNCREGISTRATION:
                var pncRedDbMap = objectMapper.convertValue(registration, Map.class);
                pncRedDbMap.putAll(inputMap);
                var ecReg = objectMapper.convertValue(pncRedDbMap, PNCRegistration.class);
                repository.save(ecReg);
                break;
            case COUPLE:
                var coupleDbMap = objectMapper.convertValue(registration.getCouple(), Map.class);
                coupleDbMap.putAll(inputMap);
                var couple = objectMapper.convertValue(coupleDbMap, Couple.class);
                registration.setCouple(couple);
                repository.save(registration);
                break;
            case MENSURALPERIOD:
                var mensuralPeriodDbMap = objectMapper.convertValue(registration.getMensuralPeriod(), Map.class);
                mensuralPeriodDbMap.putAll(inputMap);
                var mensuralPeriodDb = objectMapper.convertValue(mensuralPeriodDbMap, MensuralPeriod.class);
                registration.setMensuralPeriod(mensuralPeriodDb);
                repository.save(registration);
                break;
            case DELIVERYDETAILS:
                var deliveryDbMap = objectMapper.convertValue(registration.getDeliveryDetails(), Map.class);
                deliveryDbMap.putAll(inputMap);
                var deliveryDb = objectMapper.convertValue(deliveryDbMap, DeliveryDetails.class);
                registration.setDeliveryDetails(deliveryDb);
                repository.save(registration);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //Get the updated latest data refreshing here
        registration = getPNCReg(registrationId);
        PNCRegistrationDto registrationDto = mapper.map(registration, PNCRegistrationDto.class);
        if (kafkaEnable) {
            KafkaMessage<PNCRegistrationDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCRegistration + "Updated");
            kafkaMessage.setObject(registrationDto);
            producerClient.publishToTopic(Topics.PNCRegistration.toString(), kafkaMessage);
        }
        // Audit save
        AuditDto auditDto = buildAuditDto(registration);
        registrationDto.setAudit(auditDto);

        return registrationDto;

    }

    public PNCRegPageResponse filterPNCReg(String rchId, String regId, String dataEntryStatus, int page, int size) {
        log.debug("filter the PNCReg for rchId {} , regId {}  dataEntryStatus {}  ", rchId, regId, dataEntryStatus);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        PNCRegistration example = PNCRegistration.builder().build();
        if (StringUtils.isNotBlank(rchId)) {
            Couple coupleExample = Couple.builder().build();
            if (coupleExample != null) {
                log.debug(" couple for the pnc reg so  setting rchId in query builder  ");
                coupleExample.setRchId(rchId);
                example.setCouple(coupleExample);
            }
        }
        if (StringUtils.isNotBlank(regId)) {
            example.setId(regId);
        }
        if (StringUtils.isNotBlank(dataEntryStatus)) {
            example.setDataEntryStatus(dataEntryStatus);
        }
        return buildPNCPageResponse(repository.findAll(Example.of(example), paging));
    }

    private PNCRegPageResponse buildPNCPageResponse(Page<PNCRegistration> pncRegistrations) {
        if (pncRegistrations == null || pncRegistrations.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PNCRegPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + pncRegistrations.getTotalElements());
            Page<PNCRegistrationDto> pncRegistrationDtos = buildPNCRegDtos(pncRegistrations);
            return PNCRegPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) pncRegistrations.getTotalPages())
                            .totalElements(pncRegistrations.getTotalElements())
                            .number(pncRegistrations.getNumber())
                            .size(pncRegistrations.getSize())
                            .build())
                    .data(pncRegistrationDtos.toList())
                    .build();
        }
    }

    private Page<PNCRegistrationDto> buildPNCRegDtos(Page<PNCRegistration> pncRegistrations) {
        return pncRegistrations.map(registration -> {
            // Conversion logic
            PNCRegistrationDto registrationDto = mapper.map(registration, PNCRegistrationDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(registration);
            registrationDto.setAudit(auditDto);
            return registrationDto;
        });
    }

    public PNCRegistration getPNCReg(String registrationId) {
        return repository.findById(registrationId)
                .orElseThrow(() -> new EntityNotFoundException("registrationId ::" + registrationId + " not found"));
    }


    private AuditDto buildAuditDto(PNCRegistration registration) {
        return AuditDto.builder().createdBy(registration.getCreatedBy())
                .modifiedBy(registration.getModifiedBy())
                .dateCreated(registration.getDateCreated())
                .dateModified(registration.getDateModified())
                .build();
    }
}
