package com.ssf.inward.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.inward.constant.InwardConst;
import com.ssf.inward.dtos.*;
import com.ssf.inward.entities.IndentDetails;
import com.ssf.inward.entities.IndentItem;
import com.ssf.inward.entities.Inward;
import com.ssf.inward.entities.PoDetails;
import com.ssf.inward.exception.EntityNotFoundException;
import com.ssf.inward.kafka.KafkaProducer;
import com.ssf.inward.kafka.message.Actor;
import com.ssf.inward.kafka.message.Context;
import com.ssf.inward.kafka.message.KafkaMessage;
import com.ssf.inward.kafka.topic.Topics;
import com.ssf.inward.repository.IInwardRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class InwardService {
    @Autowired
    private IInwardRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private HttpServletRequest request;

    @Value("${kafka.enabled}")
    private Boolean kafkaEnabled;

    public InwardDto createInward(InwardDto request) {
        log.debug("Creating the inward");

        Inward inward = mapper.map(request, Inward.class);
        inward = repository.save(inward);
        InwardDto response = mapper.map(inward, InwardDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(inward);
        response.setAudit(auditDto);
        if (kafkaEnabled && response.getStatus().equalsIgnoreCase("created")) {
            KafkaMessage<InwardDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(InwardConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(InwardConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.Inward + "Created");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.Inward.toString(), kafkaMessage);
        }
        return response;
    }

    public InwardDto readInward(String id) {
        log.debug("Reading the Inward for inward id {}", id);
        auditorProvider.getCurrentAuditor();
        Inward inward = getInward(id);
        InwardDto response = mapper.map(inward, InwardDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(inward);
        response.setAudit(auditDto);

        return response;

    }

    public InwardDto patchInward(String id, InwardPatchDto request) {
        log.debug("Partial Edit for Inward orderId  {} ", id);
        var inward = getInward(id);
        var inputMap = request.getProperties();
        switch (request.getType()) {
            case INWARD:
                var inwardDbMap = objectMapper.convertValue(inward, Map.class);
                inwardDbMap.putAll(inputMap);
                var order = objectMapper.convertValue(inwardDbMap, Inward.class);
                repository.save(order);
                break;
            case PODETAILS:
                var poDetailsDbMap = objectMapper.convertValue(inward.getPoDetails(), Map.class);
                poDetailsDbMap.putAll(inputMap);
                var poDetails = objectMapper.convertValue(poDetailsDbMap, PoDetails.class);
                inward.setPoDetails(poDetails);
                repository.save(inward);
                break;
            case INDENTDETAILS:
                var indentDetailsDbMap = objectMapper.convertValue(inward.getIndentDetails(), Map.class);
                indentDetailsDbMap.putAll(inputMap);
                var indentDetails = objectMapper.convertValue(indentDetailsDbMap, IndentDetails.class);
                inward.setIndentDetails(indentDetails);
                repository.save(inward);
                break;
            case INDENTITEMS:
                List<IndentItem> items = (List<IndentItem>) inputMap.get("indentItems");
                inward.setIndentItems(items);
                repository.save(inward);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //response generate
        //Get the updated latest data refreshing here
        inward = getInward(id);
        InwardDto response = mapper.map(inward, InwardDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(inward);
        response.setAudit(auditDto);
        if (kafkaEnabled && response.getStatus().equalsIgnoreCase("created")) {
            KafkaMessage<InwardDto> kafkaMessage = new KafkaMessage<>();
            kafkaMessage.setContext(Context.builder().language("en").authToken(this.request.getHeader(InwardConst.AUTHORIZATION_HEADER)).build());
            kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(InwardConst.XUSER_ID_HEADER)).build());
            kafkaMessage.setType(Topics.Inward + "Updated");
            kafkaMessage.setObject(response);
            kafkaProducer.sendMessage(Topics.Inward.toString(), kafkaMessage);
        }
        return response;
    }

    public Inward getInward(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inward ::" + id + " not found"));
    }

    public PageResponse filterInward(LocalDate stDate, LocalDate edDate, String inwardType, String supplier, String status, int page, int size) {
        List<Criteria> criteria = new ArrayList<>();
        if (stDate != null)
            criteria.add(Criteria.where("poDate").gte(stDate));
        if (edDate != null)
            criteria.add(Criteria.where("poDate").lte(edDate));
        if (inwardType != null && (inwardType.equalsIgnoreCase("PO") || inwardType.equalsIgnoreCase("direct")))
            criteria.add(Criteria.where(("indentDetails")).isNull());
        if (inwardType != null && inwardType.equalsIgnoreCase("Indent"))
            criteria.add(Criteria.where(("poDetails")).isNull());
        if (supplier != null)
            criteria.add(Criteria.where("supplierName").in(Stream.of(supplier.split(","))
                    .collect(Collectors.toList())));
        if (status != null)
            criteria.add(Criteria.where("status").in(Stream.of(status.split(","))
                    .collect(Collectors.toList())));
        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if (!criteria.isEmpty())
            return buildPageResponse(repository.query(searchQuery, paging));
        return buildPageResponse(repository.findAll(paging));
    }

    private PageResponse buildPageResponse(Page<Inward> inward) {
        if (inward == null || inward.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Total elements in Pagination " + inward.getTotalElements());
            Page<InwardDto> dtos = buildResponseDtos(inward);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) inward.getTotalPages())
                            .totalElements(inward.getTotalElements())
                            .number(inward.getNumber())
                            .size(inward.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }

    private Page<InwardDto> buildResponseDtos(Page<Inward> inward) {
        return inward.map(p -> {
            // Conversion logic
            InwardDto dto = mapper.map(p, InwardDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            dto.setAudit(auditDto);
            return dto;
        });
    }

    private AuditDto buildAuditDto(Inward inward) {
        return AuditDto.builder().createdBy(inward.getCreatedBy())
                .modifiedBy(inward.getModifiedBy())
                .dateCreated(inward.getDateCreated())
                .dateModified(inward.getDateModified())
                .build();
    }
}
