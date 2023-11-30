package com.ssf.ancservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.ancservice.constant.ANCServiceConst;
import com.ssf.ancservice.constant.DataEntryStatusEnum;
import com.ssf.ancservice.dtos.*;
import com.ssf.ancservice.entities.ANCService;
import com.ssf.ancservice.entities.Couple;
import com.ssf.ancservice.exception.EntityNotFoundException;
import com.ssf.ancservice.kafka.KafkaProducer;
import com.ssf.ancservice.kafka.message.Actor;
import com.ssf.ancservice.kafka.message.Context;
import com.ssf.ancservice.kafka.message.KafkaMessage;
import com.ssf.ancservice.kafka.topic.Topics;
import com.ssf.ancservice.repository.IANCServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ANCServiceService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IANCServiceRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Lazy
    private KafkaProducer kafkaProducer;

    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;

    public ANCServiceDto createANC(ANCServiceDto serviceDto) {
        log.debug("Created ANC Service ");
        ANCService registration = mapper.map(serviceDto, ANCService.class);
        registration.setAudit(getCreatedByAuditor());
        registration = repository.save(registration);
        ANCServiceDto ancServiceDto = mapper.map(registration, ANCServiceDto.class);
        if (kafkaEnabled) {
            KafkaMessage<ANCServiceDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ANCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ANCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ANCService + "Created");
            kafkaMessage.setObject(ancServiceDto);
            kafkaProducer.sendMessage(Topics.ANCService.toString(), kafkaMessage);
        }
        return ancServiceDto;
    }

    public ANCServiceDto patchANC(String serviceId, ANCServicePatchDto patchDto) {
        ANCService ancService = getANCService(serviceId);
        ancService.setAudit(getModifiedByAuditor(ancService.getAudit()));
        Map<String, Object> inputMap = patchDto.getProperties();
        switch (patchDto.getType()) {
            case ANC_SERVICE:
                Map<String, Object> ancServiceMap = objectMapper.convertValue(ancService, Map.class);
                ancServiceMap.putAll(inputMap);
                ancService = objectMapper.convertValue(ancServiceMap, ANCService.class);
                ancService = repository.save(ancService);
                break;
            case COUPLE:
                Map<String, Object> coupleDbMap = objectMapper.convertValue(ancService.getCouple(), Map.class);
                coupleDbMap.putAll(inputMap);
                Couple couple = objectMapper.convertValue(coupleDbMap, Couple.class);
                ancService.setCouple(couple);
                ancService = repository.save(ancService);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //response generate
        ANCServiceDto response = mapper.map(ancService, ANCServiceDto.class);
        if (kafkaEnabled) {
            KafkaMessage<ANCServiceDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ANCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ANCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.ANCService + "Updated");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.ANCService.toString(), kafkaMessage);
        }
        return response;

    }

    public ANCService getANCService(String serviceId) {
        getCreatedByAuditor();
        return repository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("serviceId ::" + serviceId + " not found"));
    }

    public ANCPageResponse getANCByFilter(Optional<String> rchId, Optional<String> serviceId, Optional<DataEntryStatusEnum> dataEntryStatus, Integer page, Integer size) {
        log.debug("Filtering  the EC for rchId {} ,serviceId {}", rchId, serviceId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        ANCService example = ANCService.builder().build();
        Page<ANCService> ancServices;
        if (rchId.isPresent()) {
            example.setRchId(rchId.get());
        }
        if (serviceId.isPresent()) {
            example.setId(serviceId.get());
        }
        if (dataEntryStatus.isPresent()) {
            example.setDataEntryStatus(dataEntryStatus.get());
        }
        ancServices = repository.findAll(Example.of(example), paging);
        return buildANCPageResponse(ancServices);
    }

    private ANCPageResponse buildANCPageResponse(Page<ANCService> ancServices) {
        return ANCPageResponse.builder()
                .meta(PageDto.builder().totalPages(Long.valueOf(ancServices.getTotalPages())).totalElements(ancServices.getTotalElements())
                        .number(ancServices.getNumber()).size(ancServices.getSize()).build())
                .data(ancServices.getContent())
                .build();
    }

    private AuditDto getCreatedByAuditor() {
        String username = "";
        Optional<String> optional = auditorProvider.getCurrentAuditor();
        if (optional.isPresent()) {
            username = optional.get();
        }
        return AuditDto.builder().modifiedBy(username).createdBy(username).build();
    }

    private AuditDto getModifiedByAuditor(AuditDto auditDTO) {
        String username = "";
        Optional<String> optional = auditorProvider.getCurrentAuditor();
        if (optional.isPresent()) {
            username = optional.get();
        }
        auditDTO.setModifiedBy(username);
        return auditDTO;
    }
}
