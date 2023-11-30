package com.ssf.pncservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncservice.constant.PNCServiceConst;
import com.ssf.pncservice.dtos.*;
import com.ssf.pncservice.entities.Couple;
import com.ssf.pncservice.entities.PNCService;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.kafka.KafkaProducer;
import com.ssf.pncservice.kafka.message.Actor;
import com.ssf.pncservice.kafka.message.Context;
import com.ssf.pncservice.kafka.message.KafkaMessage;
import com.ssf.pncservice.kafka.topic.Topics;
import com.ssf.pncservice.repository.IPNCServiceRepository;
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
public class PNCServiceService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private IPNCServiceRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;


    public PNCServiceDto createPNC(PNCServiceDto pncServiceDto) {
        log.debug("Create PNC Service ");
        PNCService pncService = mapper.map(pncServiceDto, PNCService.class);
        pncService = repository.save(pncService);
        PNCServiceDto responseDto = mapper.map(pncService, PNCServiceDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(pncService);
        responseDto.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<PNCServiceDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCService + "Created");
            kafkaMessage.setObject(responseDto);
            kafkaProducer.sendMessage(Topics.PNCService.toString(), kafkaMessage);
        }
        return responseDto;
    }

    public PNCServiceDto patchPNC(String serviceId, PNCServicePatchDto patchDto) {
        log.debug("Updating the PNC for serviceId {}", serviceId);
        var pncService = getPNCService(serviceId);
        var inputMap = patchDto.getProperties();
        switch (patchDto.getType()) {
            case PNC_SERVICE:
                var pncServicedDbMap = objectMapper.convertValue(pncService, Map.class);
                pncServicedDbMap.putAll(inputMap);
                var ecReg = objectMapper.convertValue(pncServicedDbMap, PNCService.class);
                repository.save(ecReg);
                break;
            case COUPLE:
                var coupleDbMap = objectMapper.convertValue(pncService.getCouple(), Map.class);
                coupleDbMap.putAll(inputMap);
                var couple = objectMapper.convertValue(coupleDbMap, Couple.class);
                pncService.setCouple(couple);
                repository.save(pncService);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }

        //Get the updated latest data refreshing here
        pncService = getPNCService(serviceId);
        PNCServiceDto pncServiceDto = mapper.map(pncService, PNCServiceDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(pncService);
        pncServiceDto.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<PNCServiceDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCService + "Updated");
            kafkaMessage.setObject(pncServiceDto);
            kafkaProducer.sendMessage(Topics.PNCService.toString(), kafkaMessage);
        }
        return pncServiceDto;
    }

    public PNCServiceDto getPNC(String serviceId) {
        log.debug("Reading the PNC for service {}", serviceId);
        auditorProvider.getCurrentAuditor();
        PNCService pncService = getPNCService(serviceId);
        PNCServiceDto pncServiceDto = mapper.map(pncService, PNCServiceDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(pncService);
        pncServiceDto.setAudit(auditDto);

        return pncServiceDto;
    }

    public PNCPageResponse getPNCByFilter(String rchId, String serviceId, Integer page, Integer size) {
        log.debug("filter the PNCReg for rchId {} , serviceId {} ", rchId, serviceId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        PNCService example = PNCService.builder().build();

        if (StringUtils.isNotBlank(rchId)) {
            example.setRchId(rchId);
        }

        if (StringUtils.isNotBlank(serviceId)) {
            example.setId(serviceId);
        }

        return buildPNCPageResponse(repository.findAll(Example.of(example), paging));
    }

    public PNCService getPNCService(String serviceId) {
        return repository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("serviceId ::" + serviceId + " not found"));
    }

    private PNCPageResponse buildPNCPageResponse(Page<PNCService> pNCServices) {
        if (pNCServices == null || pNCServices.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PNCPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + pNCServices.getTotalElements());
            Page<PNCServiceDto> pNCServiceDtos = buildPNCRegDtos(pNCServices);
            return PNCPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) pNCServices.getTotalPages())
                            .totalElements(pNCServices.getTotalElements())
                            .number(pNCServices.getNumber())
                            .size(pNCServices.getSize())
                            .build())
                    .data(pNCServiceDtos.toList())
                    .build();
        }
    }

    private Page<PNCServiceDto> buildPNCRegDtos(Page<PNCService> pNCServices) {
        return pNCServices.map(registration -> {
            // Conversion logic
            PNCServiceDto pncServiceDto = mapper.map(registration, PNCServiceDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(registration);
            pncServiceDto.setAudit(auditDto);
            return pncServiceDto;
        });
    }

    private AuditDto buildAuditDto(PNCService pncService) {
        return AuditDto.builder().createdBy(pncService.getCreatedBy())
                .modifiedBy(pncService.getModifiedBy())
                .dateCreated(pncService.getDateCreated())
                .dateModified(pncService.getDateModified())
                .build();
    }
}
