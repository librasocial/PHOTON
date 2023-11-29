package com.ssf.pncregistration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import com.ssf.pncregistration.dtos.*;
import com.ssf.pncregistration.entities.Immunization;
import com.ssf.pncregistration.entities.Infant;
import com.ssf.pncregistration.entities.PNCRegistration;
import com.ssf.pncregistration.exception.EntityNotFoundException;
import com.ssf.pncregistration.exception.MissmatchException;
import com.ssf.pncregistration.kafka.producer.client.KafkaProducerClient;
import com.ssf.pncregistration.kafka.producer.model.Actor;
import com.ssf.pncregistration.kafka.producer.model.Context;
import com.ssf.pncregistration.kafka.producer.model.KafkaMessage;
import com.ssf.pncregistration.kafka.topics.Topics;
import com.ssf.pncregistration.repository.IPNCInfantRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PNCInfantService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private PNCRegistrationService pncRegistrationService;
    @Autowired
    private IPNCInfantRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private KafkaProducerClient producerClient;

    @Value("${kafka.enabled}")
    private Boolean kafkaEnable;

    public List<InfantDto> createInfant(String registrationId, List<InfantDto> infantDtos) {
        log.debug("Creating the Infant for registrationId {}", registrationId);
        if (!infantDtos.isEmpty() && !registrationId.equals(infantDtos.get(0).getPncRegistrationId())) {
            throw new MissmatchException("registrationId " + registrationId + " missmatch with request registrationId " + infantDtos.get(0).getPncRegistrationId());
        }
        PNCRegistration pncRegistration = pncRegistrationService.getPNCReg(registrationId);

        List<InfantDto> response = new ArrayList<>();
        for (InfantDto infantDto : infantDtos) {
            Infant infant = mapper.map(infantDto, Infant.class);
            repository.save(infant);
            InfantDto persistDto = mapper.map(infant, InfantDto.class);
            if (kafkaEnable) {
                KafkaMessage<PNCInfantPayload> kafkaMessage = new KafkaMessage<>();
                kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCRegistrationConst.AUTHORIZATION_HEADER)).build());
                kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCRegistrationConst.XUSER_ID_HEADER)).build());
                kafkaMessage.setType(Topics.PNCInfant + "Created");
                kafkaMessage.setObject(PNCInfantPayload.builder().infantDto(infantDto).pncRegistrationDto(mapper.map(pncRegistration, PNCRegistrationDto.class)).build());
                producerClient.publishToTopic(Topics.PNCInfant.toString(), kafkaMessage);
            }
            // Audit save
            AuditDto auditDto = buildAuditDto(infant);
            persistDto.setAudit(auditDto);
            response.add(persistDto);
        }
        return response;
    }

    public InfantDto readInfants(String registrationId, String infantId) {
        log.debug("Read the Infant for registrationId {} and infantId ", registrationId, infantId);
        auditorProvider.getCurrentAuditor();
        Infant infant = getInfant(infantId, registrationId);
        InfantDto response = mapper.map(infant, InfantDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(infant);
        response.setAudit(auditDto);

        return response;
    }

    public InfantDto patchInfant(String registrationId, String infantId, InfantPatchDto infantPatchDto) {
        log.debug("Updating the PNCInfant for registrationId {} and InfantId {}", registrationId, infantId);
        var existingInfant = getInfant(infantId, registrationId);
        var inputMap = infantPatchDto.getProperties();
        switch (infantPatchDto.getType()) {
            case INFANT:
                var infantDbMap = objectMapper.convertValue(existingInfant, Map.class);
                infantDbMap.putAll(inputMap);
                var infantEntity = objectMapper.convertValue(infantDbMap, Infant.class);
                repository.save(infantEntity);
                break;
            case IMMUNIZATION:
                var immuneDbMap = objectMapper.convertValue(existingInfant.getImmunization(), Map.class);
                immuneDbMap.putAll(inputMap);
                var immunization = objectMapper.convertValue(immuneDbMap, Immunization.class);
                existingInfant.setImmunization(immunization);
                repository.save(existingInfant);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //Get the updated latest data refreshing here
        var resultEntity = getInfant(infantId, registrationId);
        InfantDto infantDto = mapper.map(resultEntity, InfantDto.class);
        if (kafkaEnable) {
            KafkaMessage<PNCInfantPayload> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCRegistrationConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCRegistrationConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCInfant + "Updated");
            kafkaMessage.setObject(PNCInfantPayload.builder().infantDto(infantDto).pncRegistrationDto(mapper.map(pncRegistrationService.getPNCReg(registrationId), PNCRegistrationDto.class)).build());
            producerClient.publishToTopic(Topics.PNCInfant.toString(), kafkaMessage);
        }
        // Audit save
        AuditDto auditDto = buildAuditDto(resultEntity);
        infantDto.setAudit(auditDto);

        return infantDto;
    }

    public InfantPageResponse filterInfants(String infantId, String regId, int page, int size) {
        log.debug("filter the Infant for infantId {} , regId {} ", infantId, regId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        Infant example = Infant.builder().build();

        if (StringUtils.isNotBlank(infantId)) {
            example.setId(infantId);
        }
        if (StringUtils.isNotBlank(regId)) {
            example.setPncRegistrationId(regId);
        }
        return buildInfantResponse(repository.findAll(Example.of(example), paging));
    }


    public Infant getInfant(String id, String registrationId) {
        return repository.findByIdAndRegistrationId(id, registrationId)
                .orElseThrow(() -> new EntityNotFoundException("id :: registrationId " + id + " :: " + registrationId + " not found"));
    }

    private AuditDto buildAuditDto(Infant infant) {
        return AuditDto.builder().createdBy(infant.getCreatedBy())
                .modifiedBy(infant.getModifiedBy())
                .dateCreated(infant.getDateCreated())
                .dateModified(infant.getDateModified())
                .build();
    }

    private InfantPageResponse buildInfantResponse(Page<Infant> infant) {
        if (infant == null || infant.isEmpty()) {
            log.debug(" Pagination result is null ");
            return InfantPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + infant.getTotalElements());
            Page<InfantDto> infantDtos = buildInfantDtos(infant);
            return InfantPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) infant.getTotalPages())
                            .totalElements(infant.getTotalElements())
                            .number(infant.getNumber())
                            .size(infant.getSize())
                            .build())
                    .data(infantDtos.toList())
                    .build();
        }
    }

    private Page<InfantDto> buildInfantDtos(Page<Infant> infant) {
        return infant.map(registration -> {
            // Conversion logic
            InfantDto registrationDto = mapper.map(registration, InfantDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(registration);
            registrationDto.setAudit(auditDto);
            return registrationDto;
        });
    }
}
