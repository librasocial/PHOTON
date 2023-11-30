package com.ssf.baseprogram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.baseprogram.constant.BaseProgramConst;
import com.ssf.baseprogram.constant.StatusEnum;
import com.ssf.baseprogram.dtos.*;
import com.ssf.baseprogram.entities.BaseProgram;
import com.ssf.baseprogram.entities.Outcome;
import com.ssf.baseprogram.entities.Recipient;
import com.ssf.baseprogram.entities.Schedule;
import com.ssf.baseprogram.exception.EntityNotFoundException;
import com.ssf.baseprogram.kafka.KafkaProducer;
import com.ssf.baseprogram.kafka.message.Actor;
import com.ssf.baseprogram.kafka.message.Context;
import com.ssf.baseprogram.kafka.message.KafkaMessage;
import com.ssf.baseprogram.kafka.topic.Topics;
import com.ssf.baseprogram.repository.IBaseProgramRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BaseProgramService {
    @Autowired
    private IBaseProgramRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private KafkaProducer kafkaProducer;

    public BaseProgramDto create(BaseProgramDto request) {
        log.debug("Creating the baseprogram");

        BaseProgram baseProgram = mapper.map(request, BaseProgram.class);
        repository.save(baseProgram);
        BaseProgramDto response = mapper.map(baseProgram, BaseProgramDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(baseProgram);
        response.setAudit(auditDto);
        if (response.getSchedule().getStatus().equals(StatusEnum.SCHEDULED) || response.getSchedule().getStatus().equals(StatusEnum.RESCHEDULED)) {
            KafkaMessage<BaseProgramDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(BaseProgramConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(BaseProgramConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.BaseProgram + "Created");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.BaseProgram.toString(), kafkaMessage);
        }
        return response;
    }

    public BaseProgramDto read(String id) {
        log.debug("Reading the BaseProgram for id {}", id);
        auditorProvider.getCurrentAuditor();
        BaseProgram baseProgram = getBaseProgram(id);
        BaseProgramDto response = mapper.map(baseProgram, BaseProgramDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(baseProgram);
        response.setAudit(auditDto);

        return response;

    }

    public BaseProgramDto patch(String id, BaseProgramPatchDto request) {
        log.debug("Partial Edit for BaseProgram id  {} ", id);
        var baseProgram = getBaseProgram(id);
        var inputMap = request.getProperties();
        switch (request.getType()) {
            case BASEPROGRAM:
                var baseDbMap = objectMapper.convertValue(baseProgram, Map.class);
                baseDbMap.putAll(inputMap);
                var order = objectMapper.convertValue(baseDbMap, BaseProgram.class);
                repository.save(order);
                break;
            case SCHEDULE:
                var scDbMap = objectMapper.convertValue(baseProgram.getSchedule(), Map.class);
                scDbMap.putAll(inputMap);
                var schedule = objectMapper.convertValue(scDbMap, Schedule.class);
                baseProgram.setSchedule(schedule);
                repository.save(baseProgram);
                break;
            case RECIPIENTS:
                List<Recipient> recipients = (List<Recipient>) inputMap.get("recipients");
                baseProgram.setRecipients(recipients);
                repository.save(baseProgram);
            case OUTCOMES:
                List<Outcome> outcomes = (List<Outcome>) inputMap.get("outcomes");
                baseProgram.setOutcomes(outcomes);
                repository.save(baseProgram);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        // response generate
        // Get the updated latest data refreshing here
        baseProgram = getBaseProgram(id);
        BaseProgramDto response = mapper.map(baseProgram, BaseProgramDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(baseProgram);
        response.setAudit(auditDto);

        if (response.getSchedule().getStatus().equals(StatusEnum.SCHEDULED) || response.getSchedule().getStatus().equals(StatusEnum.RESCHEDULED)) {
            KafkaMessage<BaseProgramDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(BaseProgramConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(BaseProgramConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.BaseProgram + "Updated");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.BaseProgram.toString(), kafkaMessage);
        }
        return response;
    }

    public BaseProgram getBaseProgram(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Baseprogram ::" + id + " not found"));
    }

    public PageResponse filter(LocalDate stDate, LocalDate edDate, String activityName, String programType,
                               String village, String location, int page, int size) {
        List<Criteria> criteria = new ArrayList<>();
        if (stDate != null)
            criteria.add(Criteria.where("meetingDate").gte(stDate));
        if (edDate != null)
            criteria.add(Criteria.where("meetingDate").lt(edDate));
        if (activityName != null)
            criteria.add(Criteria.where(("activityName")).is(activityName));
        if (programType != null)
            criteria.add(Criteria.where("programType").is(programType));
        if (village != null)
            criteria.add(Criteria.where("village").is(village));
        if (location != null)
            criteria.add(Criteria.where("location").is(location));
        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if (!criteria.isEmpty())
            return buildPageResponse(repository.query(searchQuery, paging));
        return buildPageResponse(repository.findAll(paging));
    }

    private PageResponse buildPageResponse(Page<BaseProgram> basePrograms) {
        if (basePrograms == null || basePrograms.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>()).build();

        } else {
            log.debug(" Pagination result is returned " + basePrograms.getTotalElements());
            Page<BaseProgramDto> dtos = buildBaseProgramDtos(basePrograms);
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages((long) basePrograms.getTotalPages())
                            .totalElements(basePrograms.getTotalElements()).number(basePrograms.getNumber())
                            .size(basePrograms.getSize()).build())
                    .data(dtos.toList()).build();
        }
    }

    private Page<BaseProgramDto> buildBaseProgramDtos(Page<BaseProgram> baseProgram) {
        return baseProgram.map(p -> {
            // Conversion logic
            BaseProgramDto dto = mapper.map(p, BaseProgramDto.class);
            // Audit save
            AuditDto auditDto = buildAuditDto(p);
            dto.setAudit(auditDto);
            return dto;
        });
    }

    private AuditDto buildAuditDto(BaseProgram baseProgram) {
        return AuditDto.builder().createdBy(baseProgram.getCreatedBy()).modifiedBy(baseProgram.getModifiedBy())
                .dateCreated(baseProgram.getDateCreated()).dateModified(baseProgram.getDateModified()).build();
    }
}
