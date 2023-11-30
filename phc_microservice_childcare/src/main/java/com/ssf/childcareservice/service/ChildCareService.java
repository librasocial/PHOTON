package com.ssf.childcareservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.childcareservice.constant.ChildCareServiceConst;
import com.ssf.childcareservice.dtos.*;
import com.ssf.childcareservice.entities.ChildCare;
import com.ssf.childcareservice.exception.EntityNotFoundException;
import com.ssf.childcareservice.kafka.KafkaProducer;
import com.ssf.childcareservice.kafka.message.Actor;
import com.ssf.childcareservice.kafka.message.Context;
import com.ssf.childcareservice.kafka.message.KafkaMessage;
import com.ssf.childcareservice.kafka.topic.Topics;
import com.ssf.childcareservice.repository.IChildCareRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class ChildCareService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private IChildCareRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private KafkaProducer kafkaProducer;

    public ChildCareDto createChildCare(ChildCareDto request) {
        log.debug("Create child care Service ");
        ChildCare childCare = mapper.map(request, ChildCare.class);
        repository.save(childCare);
        ChildCareDto responseDto = mapper.map(childCare, ChildCareDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(childCare);
        responseDto.setAudit(auditDto);
        KafkaMessage<ChildCareDto> kafkaMessage = new KafkaMessage<>();
        kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ChildCareServiceConst.AUTHORIZATION_HEADER)).build());
        kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ChildCareServiceConst.XUSER_ID_HEADER)).build());
        kafkaMessage.setType(Topics.ChildCare + "Created");
        kafkaMessage.setObject(responseDto);
        kafkaProducer.sendMessage(Topics.ChildCare.toString(), kafkaMessage);
        return responseDto;
    }

    public ChildCareDto readChildCare(String childCareId) {
        log.debug("Reading the Childcare for id {}", childCareId);
        auditorProvider.getCurrentAuditor();
        ChildCare childCare = getChildCare(childCareId);
        ChildCareDto responseDto = mapper.map(childCare, ChildCareDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(childCare);
        responseDto.setAudit(auditDto);
        return responseDto;
    }

    public ChildCareDto patchChildCare(String id, Map<String, Object> request) {
        log.debug("Updating the Childcare for  id {}", id);
        ChildCare existingChildCare = getChildCare(id);

        // copy the fields which are changed to entity object
        Map<String, Object> dbMap = objectMapper.convertValue(existingChildCare, Map.class);
        dbMap.putAll(request);
        existingChildCare = objectMapper.convertValue(dbMap, ChildCare.class);
        repository.save(existingChildCare);

        //response generate
        var response = mapper.map(existingChildCare, ChildCareDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(existingChildCare);
        response.setAudit(auditDto);
        KafkaMessage<ChildCareDto> kafkaMessage = new KafkaMessage<>();
        kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(ChildCareServiceConst.AUTHORIZATION_HEADER)).build());
        kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(ChildCareServiceConst.XUSER_ID_HEADER)).build());
        kafkaMessage.setType(Topics.ChildCare + "Updated");
        kafkaMessage.setObject(response);
        kafkaProducer.sendMessage(Topics.ChildCare.toString(), kafkaMessage);
        return response;
    }

    private ChildCare getChildCare(String childCareId) {
        return repository.findById(childCareId)
                .orElseThrow(() -> new EntityNotFoundException("childCareId ::" + childCareId + " not found"));
    }

    public PageResponse getChildCareByFilter(String citizenId, String childId, Integer page, Integer size) {
        log.debug("filter the ChildCare by citizenId {},  childId  {} ", citizenId, childId);

        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        ChildCare example = ChildCare.builder().build();

        if (StringUtils.isNotBlank(citizenId)) {
            example.setCitizenId(citizenId);
        }
        if (StringUtils.isNotBlank(childId)) {
            example.setChildId(childId);
        }

        return buildChildCarePageResponse(repository.findAll(Example.of(example), paging));
    }

    private PageResponse buildChildCarePageResponse(Page<ChildCare> childCares) {
        if (childCares == null || childCares.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + childCares.getTotalElements());
            Page<ChildCareDto> infantDtoPage = buildPChildCareDtos(childCares);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) childCares.getTotalPages())
                            .totalElements(childCares.getTotalElements())
                            .number(childCares.getNumber())
                            .size(childCares.getSize())
                            .build())
                    .data(infantDtoPage.toList())
                    .build();
        }
    }

    private Page<ChildCareDto> buildPChildCareDtos(Page<ChildCare> childCares) {
        return childCares.map(i -> {
            // Conversion logic
            ChildCareDto childCareDto = mapper.map(i, ChildCareDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(i);
            childCareDto.setAudit(auditDto);
            return childCareDto;
        });
    }

    private AuditDto buildAuditDto(ChildCare childCare) {
        return AuditDto.builder().createdBy(childCare.getCreatedBy())
                .modifiedBy(childCare.getModifiedBy())
                .dateCreated(childCare.getDateCreated())
                .dateModified(childCare.getDateModified())
                .build();
    }
}
