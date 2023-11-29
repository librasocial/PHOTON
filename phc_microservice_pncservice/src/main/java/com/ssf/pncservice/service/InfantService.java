package com.ssf.pncservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncservice.constant.PNCServiceConst;
import com.ssf.pncservice.dtos.AuditDto;
import com.ssf.pncservice.dtos.InfantDto;
import com.ssf.pncservice.dtos.InfantPageResponse;
import com.ssf.pncservice.dtos.PageDto;
import com.ssf.pncservice.entities.Infant;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.exception.MissmatchException;
import com.ssf.pncservice.kafka.KafkaProducer;
import com.ssf.pncservice.kafka.message.Actor;
import com.ssf.pncservice.kafka.message.Context;
import com.ssf.pncservice.kafka.message.KafkaMessage;
import com.ssf.pncservice.kafka.topic.Topics;
import com.ssf.pncservice.repository.IInfantRepository;
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
public class InfantService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private PNCServiceService pncServiceService;
    @Autowired
    private IInfantRepository repository;
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


    public InfantDto createInfant(String serviceId, InfantDto infantDto) {
        log.debug("Create Infant Service ");
        pncServiceService.getPNCService(serviceId);
        if (!serviceId.equals(infantDto.getPncServiceId())) {
            throw new MissmatchException("serviceId " + serviceId + " missmatch with request service id " + infantDto.getPncServiceId());
        }
        Infant infant = mapper.map(infantDto, Infant.class);
        repository.save(infant);
        InfantDto responseDto = mapper.map(infant, InfantDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(infant);
        responseDto.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<InfantDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCInfantService + "Created");
            kafkaMessage.setObject(responseDto);
            kafkaProducer.sendMessage(Topics.PNCInfantService.toString(), kafkaMessage);
        }
        return responseDto;
    }
    public InfantDto patchInfant(String serviceId, String infantId, Map<String, Object> patchDto) {
        log.debug("Updating the Infant for serviceId {} , infantId {}", serviceId, infantId);
        var infant = getInfantByIdServiceId(infantId, serviceId);

        var infantDbMap = objectMapper.convertValue(infant, Map.class);
        infantDbMap.putAll(patchDto);
        var infantEntity = objectMapper.convertValue(infantDbMap, Infant.class);
        repository.save(infantEntity);

        var response = mapper.map(infantEntity, InfantDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(infantEntity);
        response.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<InfantDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(PNCServiceConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(PNCServiceConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.PNCInfantService + "Updated");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.PNCInfantService.toString(), kafkaMessage);
        }
        return response;

    }

    public InfantDto getInfant(String serviceId, String infantId) {
        log.debug("Reading the Infant for service {}, infantId {} ", serviceId, infantId);
        auditorProvider.getCurrentAuditor();
        Infant infant = getInfantByIdServiceId(infantId, serviceId);
        InfantDto infantDto = mapper.map(infant, InfantDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(infant);
        infantDto.setAudit(auditDto);

        return infantDto;
    }

    public InfantPageResponse getInfantByFilter(String childId, String serviceId, String infantId, Integer page, Integer size) {
        log.debug("filter the PNCReg for childId {} , serviceId {}, infantId {}  ", childId, serviceId, infantId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        Infant example = Infant.builder().build();

        if (StringUtils.isNotBlank(childId)) {
            example.setChildId(childId);
        }

        if (StringUtils.isNotBlank(serviceId)) {
            example.setPncServiceId(serviceId);
        }

        if (StringUtils.isNotBlank(infantId)) {
            example.setId(infantId);
        }

        return buildInfantPageResponse(repository.findAll(Example.of(example), paging));
    }

    public Infant getInfantByIdServiceId(String id, String pncServiceId) {
        return repository.findByIdAndServiceId(id, pncServiceId)
                .orElseThrow(() -> new EntityNotFoundException("pncServiceId ::" + pncServiceId + " not found"));
    }

    private InfantPageResponse buildInfantPageResponse(Page<Infant> infants) {
        if (infants == null || infants.isEmpty()) {
            log.debug(" Pagination result is null ");
            return InfantPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + infants.getTotalElements());
            Page<InfantDto> infantDtoPage = buildPInfantDtos(infants);
            return InfantPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) infants.getTotalPages())
                            .totalElements(infants.getTotalElements())
                            .number(infants.getNumber())
                            .size(infants.getSize())
                            .build())
                    .data(infantDtoPage.toList())
                    .build();
        }
    }

    private Page<InfantDto> buildPInfantDtos(Page<Infant> infant) {
        return infant.map(i -> {
            // Conversion logic
            InfantDto infantDto = mapper.map(i, InfantDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(i);
            infantDto.setAudit(auditDto);
            return infantDto;
        });
    }

    private AuditDto buildAuditDto(Infant infant) {
        return AuditDto.builder().createdBy(infant.getCreatedBy())
                .modifiedBy(infant.getModifiedBy())
                .dateCreated(infant.getDateCreated())
                .dateModified(infant.getDateModified())
                .build();
    }
}
