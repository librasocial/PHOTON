package com.ssf.idspsurveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.idspsurveillance.constant.IDSPConstant;
import com.ssf.idspsurveillance.dtos.*;
import com.ssf.idspsurveillance.entites.*;
import com.ssf.idspsurveillance.exception.EntityNotFoundException;
import com.ssf.idspsurveillance.kafka.KafkaProducer;
import com.ssf.idspsurveillance.kafka.message.Actor;
import com.ssf.idspsurveillance.kafka.message.Context;
import com.ssf.idspsurveillance.kafka.message.KafkaMessage;
import com.ssf.idspsurveillance.kafka.topic.Topics;
import com.ssf.idspsurveillance.repository.IIDSPSurveillanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class IDSPSurveillanceService {
    @Autowired
    private IIDSPSurveillanceRepository repository;
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

    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;

    public IDSPSurveillanceDto create(IDSPSurveillanceDto request) {
        log.debug("Creating the idspsurveillance");

        IDSPSurveillance idspsurveillance = mapper.map(request, IDSPSurveillance.class);
        repository.save(idspsurveillance);
        IDSPSurveillanceDto response = mapper.map(idspsurveillance, IDSPSurveillanceDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(idspsurveillance);
        response.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<IDSPSurveillanceDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(IDSPConstant.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(IDSPConstant.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.IDSPSurveillance + "Created");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.IDSPSurveillance.toString(), kafkaMessage);
        }
        return response;
    }

    public IDSPSurveillanceDto read(String id) {
        log.debug("Reading the IDSPSurveillance for idspsurveillance id {}", id);
        auditorProvider.getCurrentAuditor();
        IDSPSurveillance idspsurveillance = getIDSP(id);
        IDSPSurveillanceDto response = mapper.map(idspsurveillance, IDSPSurveillanceDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(idspsurveillance);
        response.setAudit(auditDto);

        return response;

    }
    public IDSPSurveillanceDto patch(String id, PatchDto request) {
        log.debug("Partial Edit for IDSPSurveillance orderId  {} ", id);
        var idspsurveillance = getIDSP(id);
        var inputMap = request.getProperties();
        switch (request.getType()) {
            case IDSP:
                var idspDbMap = objectMapper.convertValue(idspsurveillance, Map.class);
                idspDbMap.putAll(inputMap);
                var idsp = objectMapper.convertValue(idspDbMap, IDSPSurveillance.class);
                repository.save(idsp);
                break;
            case FEVER:
                var feverDbMap = objectMapper.convertValue(idspsurveillance.getFever(), Map.class);
                feverDbMap.putAll(inputMap);
                var fever = objectMapper.convertValue(feverDbMap, Fever.class);
                idspsurveillance.setFever(fever);
                repository.save(idspsurveillance);
                break;
            case COUGH:
                var coughDbMap = objectMapper.convertValue(idspsurveillance.getCough(), Map.class);
                coughDbMap.putAll(inputMap);
                var cough = objectMapper.convertValue(coughDbMap, Cough.class);
                idspsurveillance.setCough(cough);
                repository.save(idspsurveillance);
                break;
            case LOOSESTOOLS:
                var stoolsDbMap = objectMapper.convertValue(idspsurveillance.getLooseStools(), Map.class);
                stoolsDbMap.putAll(inputMap);
                var stools = objectMapper.convertValue(stoolsDbMap, LooseStools.class);
                idspsurveillance.setLooseStools(stools);
                repository.save(idspsurveillance);
                break;
            case JAUNDICE:
                var jaundiceDbMap = objectMapper.convertValue(idspsurveillance.getJaundice(), Map.class);
                jaundiceDbMap.putAll(inputMap);
                var jaundice = objectMapper.convertValue(jaundiceDbMap, Jaundice.class);
                idspsurveillance.setJaundice(jaundice);
                repository.save(idspsurveillance);
                break;
            case ANIMALBITE:
                var biteDbMap = objectMapper.convertValue(idspsurveillance.getAnimalBite(), Map.class);
                biteDbMap.putAll(inputMap);
                var bite = objectMapper.convertValue(biteDbMap, AnimalBite.class);
                idspsurveillance.setAnimalBite(bite);
                repository.save(idspsurveillance);
                break;
            case OTHERS:
                var othDbMap = objectMapper.convertValue(idspsurveillance.getOthers(), Map.class);
                othDbMap.putAll(inputMap);
                var other = objectMapper.convertValue(othDbMap, Others.class);
                idspsurveillance.setOthers(other);
                repository.save(idspsurveillance);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //response generate
        //Get the updated latest data refreshing here
        idspsurveillance = getIDSP(id);
        IDSPSurveillanceDto response = mapper.map(idspsurveillance, IDSPSurveillanceDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(idspsurveillance);
        response.setAudit(auditDto);
        if (kafkaEnabled) {
            KafkaMessage<IDSPSurveillanceDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(IDSPConstant.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(IDSPConstant.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.IDSPSurveillance + "Updated");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.IDSPSurveillance.toString(), kafkaMessage);
        }
        return response;
    }

    public IDSPSurveillance getIDSP(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("IDSPSurveillance ::" + id + " not found"));
    }

    public PageResponse filter(String citizenId, LocalDate stDate, LocalDate edDate, int page, int size) {
        List<Criteria> criteria = new ArrayList<>();
        if (citizenId != null)
            criteria.add(Criteria.where("citizenId").is(citizenId));
        if (stDate != null)
            criteria.add(Criteria.where("dateOfSurveillance").gte(stDate));
        if (edDate != null)
            criteria.add(Criteria.where("dateOfSurveillance").lt(edDate));
        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if (!criteria.isEmpty())
            return buildPageResponse(repository.query(searchQuery, paging));
        return buildPageResponse(repository.findAll(paging));
    }

    private PageResponse buildPageResponse(Page<IDSPSurveillance> idspsurveillance) {
        if (idspsurveillance == null || idspsurveillance.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Total elements in Pagination " + idspsurveillance.getTotalElements());
            Page<IDSPSurveillanceDto> dtos = buildResponseDtos(idspsurveillance);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) idspsurveillance.getTotalPages())
                            .totalElements(idspsurveillance.getTotalElements())
                            .number(idspsurveillance.getNumber())
                            .size(idspsurveillance.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }

    private Page<IDSPSurveillanceDto> buildResponseDtos(Page<IDSPSurveillance> idspsurveillance) {
        return idspsurveillance.map(p -> {
            // Conversion logic
            IDSPSurveillanceDto dto = mapper.map(p, IDSPSurveillanceDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            dto.setAudit(auditDto);
            return dto;
        });
    }

    private AuditDto buildAuditDto(IDSPSurveillance idspsurveillance) {
        return AuditDto.builder().createdBy(idspsurveillance.getCreatedBy())
                .modifiedBy(idspsurveillance.getModifiedBy())
                .dateCreated(idspsurveillance.getDateCreated())
                .dateModified(idspsurveillance.getDateModified())
                .build();
    }
}
