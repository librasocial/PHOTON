package com.ssf.ancregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancregistration.constant.ANCRegistrationConst;
import com.ssf.ancregistration.dtos.*;
import com.ssf.ancregistration.entities.*;
import com.ssf.ancregistration.exception.EntityNotFoundException;
import com.ssf.ancregistration.kafka.KafkaProducer;
import com.ssf.ancregistration.kafka.message.Actor;
import com.ssf.ancregistration.kafka.message.Context;
import com.ssf.ancregistration.kafka.message.KafkaMessage;
import com.ssf.ancregistration.kafka.topic.Topics;
import com.ssf.ancregistration.repository.IANCRegistrationRepository;
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
public class ANCRegistrationService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IANCRegistrationRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;

    public ANCRegistrationDto registration(ANCRegistrationDto registrationDto) {
        log.debug("Registration for ANC ");
        ANCRegistration registration = mapper.map(registrationDto, ANCRegistration.class);
        repository.save(registration);
        ANCRegistrationDto ancRegistrationDto = mapper.map(registration, ANCRegistrationDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(registration);
        ancRegistrationDto.setAudit(auditDto);

        if (kafkaEnabled) {
            KafkaMessage<ANCRegistrationDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ANCRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ANCRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ANCRegistration + "Created");
            kafkaMessage.setObject(ancRegistrationDto);
            kafkaProducer.sendMessage(Topics.ANCRegistration.toString(), kafkaMessage);
        }

        return ancRegistrationDto;
    }

    public ANCRegistrationDto readANC(String registrationId) {
        log.debug("Reading the ANC for registrationId {}", registrationId);
        auditorProvider.getCurrentAuditor();
        ANCRegistration registration = getANCReg(registrationId);
        ANCRegistrationDto registrationDto = mapper.map(registration, ANCRegistrationDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(registration);
        registrationDto.setAudit(auditDto);

        return registrationDto;
    }

    public ANCRegistrationDto patchANC(String registrationId, ANCRegPatchDto ancRequest) {
        log.debug("Updating the ANC for registrationId {}", registrationId);
        var registration = getANCReg(registrationId);
        var inputMap = ancRequest.getProperties();
        switch (ancRequest.getType()) {
            case ANCREGISTRATION:
                var ancRedDbMap = objectMapper.convertValue(registration, Map.class);
                ancRedDbMap.putAll(inputMap);
                var ecReg = objectMapper.convertValue(ancRedDbMap, ANCRegistration.class);
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
            case PREG:
                //For pregnancy we are just adding new preg to existing list no partial update for current pregnancy
                var existingPreg = registration.getPregnancies();
                var newPreg = objectMapper.convertValue(inputMap, Pregnancy.class);
                existingPreg.add(newPreg);
                registration.setPregnancies(existingPreg);
                repository.save(registration);
                break;
            case PREGWOMAN:
                var pregWDbMap = objectMapper.convertValue(registration.getPregnantWoman(), Map.class);
                pregWDbMap.putAll(inputMap);
                var pregWDb = objectMapper.convertValue(pregWDbMap, PregnantWoman.class);
                registration.setPregnantWoman(pregWDb);
                repository.save(registration);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //Get the updated latest data refreshing here
        registration = getANCReg(registrationId);
        ANCRegistrationDto registrationDto = mapper.map(registration, ANCRegistrationDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(registration);
        registrationDto.setAudit(auditDto);

        if (kafkaEnabled) {
            KafkaMessage<ANCRegistrationDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ANCRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ANCRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ANCRegistration + "Updated");
            kafkaMessage.setObject(registrationDto);
            kafkaProducer.sendMessage(Topics.ANCRegistration.toString(), kafkaMessage);
        }

        return registrationDto;

    }

    public ANCRegPageResponse filterANCReg(String rchId, String regId, String dataEntryStatus, int page, int size) {
        log.debug("filter the ANCReg for rchId {} , regId {}  dataEntryStatus {}  ", rchId, regId, dataEntryStatus);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        ANCRegistration example = ANCRegistration.builder().build();
        if (StringUtils.isNotBlank(rchId)) {
            Couple coupleExample = Couple.builder().build();
            if (coupleExample != null) {
                log.debug(" couple for the anc reg so  setting rchId in query builder  ");
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
        return buildANCPageResponse(repository.findAll(Example.of(example), paging));
    }

    private ANCRegPageResponse buildANCPageResponse(Page<ANCRegistration> ancRegistrations) {
        if (ancRegistrations == null || ancRegistrations.isEmpty()) {
            log.debug(" Pagination result is null ");
            return ANCRegPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + ancRegistrations.getTotalElements());
            Page<ANCRegistrationDto> ancRegistrationDtos = buildANCRegDtos(ancRegistrations);
            return ANCRegPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) ancRegistrations.getTotalPages())
                            .totalElements(ancRegistrations.getTotalElements())
                            .number(ancRegistrations.getNumber())
                            .size(ancRegistrations.getSize())
                            .build())
                    .data(ancRegistrationDtos.toList())
                    .build();
        }
    }

    private Page<ANCRegistrationDto> buildANCRegDtos(Page<ANCRegistration> ancRegistrations) {
        return ancRegistrations.map(registration -> {
            // Conversion logic
            ANCRegistrationDto registrationDto = mapper.map(registration, ANCRegistrationDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(registration);
            registrationDto.setAudit(auditDto);
            return registrationDto;
        });
    }

    public ANCRegistration getANCReg(String registrationId) {
        return repository.findById(registrationId)
                .orElseThrow(() -> new EntityNotFoundException("registrationId ::" + registrationId + " not found"));
    }


    private AuditDto buildAuditDto(ANCRegistration registration) {
        return AuditDto.builder().createdBy(registration.getCreatedBy())
                .modifiedBy(registration.getModifiedBy())
                .dateCreated(registration.getDateCreated())
                .dateModified(registration.getDateModified())
                .build();
    }
}
