package com.ssf.ecregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import com.ssf.ecregistration.dtos.*;
import com.ssf.ecregistration.entities.Couple;
import com.ssf.ecregistration.entities.CoupleAdditionalDetails;
import com.ssf.ecregistration.entities.EligibleRegistration;
import com.ssf.ecregistration.entities.RchGeneration;
import com.ssf.ecregistration.exception.EntityNotFoundException;
import com.ssf.ecregistration.kafka.producer.client.KafkaProducerClient;
import com.ssf.ecregistration.kafka.producer.model.Actor;
import com.ssf.ecregistration.kafka.producer.model.Context;
import com.ssf.ecregistration.kafka.producer.model.KafkaMessage;
import com.ssf.ecregistration.kafka.topics.Topics;
import com.ssf.ecregistration.repository.IEligibleRegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class EligibleRegistrationService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private IEligibleRegistrationRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaProducerClient producerClient;
    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;

    public EligibleRegistrationDto registration(EligibleRegistrationDto registrationDto) {
        log.debug("Registration for Eligible Couple");
        EligibleRegistration registration = mapper.map(registrationDto, EligibleRegistration.class);
        repository.save(registration);
        EligibleRegistrationDto eligibleRegistrationDtoResult = mapper.map(registration, EligibleRegistrationDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(registration);
        eligibleRegistrationDtoResult.setAudit(auditDto);
        //Need to check the amount of time it takes
        if (kafkaEnabled) {
            KafkaMessage<EligibleRegistrationDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(request.getHeader(EligibleRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(request.getHeader(EligibleRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ECRegistration + "Created");
            kafkaMessage.setObject(eligibleRegistrationDtoResult);
            producerClient.publishToTopic(Topics.ECRegistration.toString(), kafkaMessage);
        }
        return eligibleRegistrationDtoResult;
    }

    public EligibleRegistrationDto registrationPatch(String registrationId, EligibleRegPatchDto request) {
        log.debug("Partial Edit Registration for Eligible Couple registration id {} ", registrationId);
        var registration = getRegistration(registrationId);
        var inputMap = request.getProperties();
        switch (request.getType()) {
            case ECRegistration:
                var ecRedDbMap = objectMapper.convertValue(registration, Map.class);
                ecRedDbMap.putAll(inputMap);
                var ecReg = objectMapper.convertValue(ecRedDbMap, EligibleRegistration.class);
                repository.save(ecReg);
                break;
            case Couple:
                var coupleDbMap = objectMapper.convertValue(registration.getCouple(), Map.class);
                coupleDbMap.putAll(inputMap);
                var couple = objectMapper.convertValue(coupleDbMap, Couple.class);
                registration.setCouple(couple);
                repository.save(registration);
                break;
            case CoupleAdditionalDetails:
                var coupleAddDbMap = objectMapper.convertValue(registration.getCoupleAdditionalDetails(), Map.class);
                coupleAddDbMap.putAll(inputMap);
                var coupleAdd = objectMapper.convertValue(coupleAddDbMap, CoupleAdditionalDetails.class);
                registration.setCoupleAdditionalDetails(coupleAdd);
                repository.save(registration);
                break;
            case RchGeneration:
                var rchDbMap = objectMapper.convertValue(registration.getRchGeneration(), Map.class);
                rchDbMap.putAll(inputMap);
                var rch = objectMapper.convertValue(rchDbMap, RchGeneration.class);
                registration.setRchGeneration(rch);
                repository.save(registration);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }

        EligibleRegistrationDto eligibleRegistrationDtoResult = mapper.map(registration, EligibleRegistrationDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(registration);
        eligibleRegistrationDtoResult.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<EligibleRegistrationDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(EligibleRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(EligibleRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ECRegistration + "Updated");
            kafkaMessage.setObject(eligibleRegistrationDtoResult);
            producerClient.publishToTopic(Topics.ECRegistration.toString(), kafkaMessage);
        }
        return eligibleRegistrationDtoResult;
    }

    public EligibleRegistrationDto getECRegistrationById(String registrationId) {
        log.debug("Reading the EC for serviceId {}", registrationId);
        auditorProvider.getCurrentAuditor();
        EligibleRegistration registration = getRegistration(registrationId);
        EligibleRegistrationDto registrationDto = mapper.map(registration, EligibleRegistrationDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(registration);
        registrationDto.setAudit(auditDto);

        return registrationDto;
    }

    public EligibleRegPageResponse filterEcReg(String rchId, String regId, String dataEntryStatus, int page, int size) {
        log.debug("Filtering  the ECReg  for rchId {} ,regId {}", rchId, regId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        Page<EligibleRegistration> registrationPage;
        if (Objects.nonNull(rchId) && Objects.nonNull(regId)) {
            registrationPage = repository.findByIdAndRchId(regId, rchId, paging);
        } else if (Objects.nonNull(rchId)) {
            registrationPage = repository.findByRchId(rchId, paging);
        } else if (Objects.nonNull(regId)) {
            registrationPage = repository.findById(regId, paging);
        } else if (Objects.nonNull(dataEntryStatus)) {
            registrationPage = repository.findByDataEntryStatus(dataEntryStatus, paging);
        } else {
            registrationPage = repository.findAll(paging);
        }
        return buildECRegDtoResponse(registrationPage);

    }

    private EligibleRegPageResponse buildECRegDtoResponse(Page<EligibleRegistration> registrationPages) {
        if (registrationPages == null || registrationPages.isEmpty()) {
            log.debug(" Pagination result is null ");
            return EligibleRegPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned ");
            Page<EligibleRegistrationDto> registrationDtos = buildECRegDto(registrationPages);
            return EligibleRegPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages(Long.valueOf(registrationPages.getTotalPages()))
                            .totalElements(Long.valueOf(registrationPages.getTotalElements()))
                            .number(registrationPages.getNumber())
                            .size(registrationPages.getSize())
                            .build())
                    .data(registrationDtos.toList())
                    .build();
        }
    }

    private Page<EligibleRegistrationDto> buildECRegDto(Page<EligibleRegistration> registrationPages) {
        return registrationPages.map(registration -> {
            // Conversion logic
            EligibleRegistrationDto registrationDto = mapper.map(registration, EligibleRegistrationDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(registration);
            registrationDto.setAudit(auditDto);
            return registrationDto;
        });
    }

    private EligibleRegistration getRegistration(String registrationId) {
        return repository.findById(registrationId)
                .orElseThrow(() -> new EntityNotFoundException("registrationId ::" + registrationId + " not found"));
    }

    private AuditDto buildAuditDto(EligibleRegistration registration) {
        return AuditDto.builder().createdBy(registration.getCreatedBy())
                .modifiedBy(registration.getModifiedBy())
                .dateCreated(registration.getDateCreated())
                .dateModified(registration.getDateModified())
                .build();
    }

}
