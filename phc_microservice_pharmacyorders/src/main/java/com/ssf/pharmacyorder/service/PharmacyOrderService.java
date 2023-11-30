package com.ssf.pharmacyorder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pharmacyorder.dtos.*;
import com.ssf.pharmacyorder.entities.Encounter;
import com.ssf.pharmacyorder.entities.Items;
import com.ssf.pharmacyorder.entities.Patient;
import com.ssf.pharmacyorder.entities.PharmacyOrder;
import com.ssf.pharmacyorder.exception.EntityNotFoundException;
import com.ssf.pharmacyorder.repository.IPharmacyOrderRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class PharmacyOrderService {
    @Autowired
    private IPharmacyOrderRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    public PharmacyOrderDto createProduct(PharmacyOrderDto request) {
        log.debug("Creating the pharmacyOrder");

        PharmacyOrder pharmacyOrder = mapper.map(request, PharmacyOrder.class);
        repository.save(pharmacyOrder);
        PharmacyOrderDto response = mapper.map(pharmacyOrder, PharmacyOrderDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(pharmacyOrder);
        response.setAudit(auditDto);
        return response;
    }

    public PharmacyOrderDto readProduct(String id) {
        log.debug("Reading the PharmacyOrder for id {}", id);
        auditorProvider.getCurrentAuditor();
        PharmacyOrder pharmacyOrder = getPharmacyOrder(id);
        PharmacyOrderDto response = mapper.map(pharmacyOrder, PharmacyOrderDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(pharmacyOrder);
        response.setAudit(auditDto);

        return response;

    }

    public PharmacyOrderDto pharmacyOrderPatch(String orderId, PharmacyOrderPatchDto request) {
        log.debug("Partial Edit for PharmacyOrder orderId  {} ", orderId);
        var pharmacyOrder = getPharmacyOrder(orderId);
        var inputMap = request.getProperties();
        switch (request.getType()) {
            case ORDER:
                var orderDbMap = objectMapper.convertValue(pharmacyOrder, Map.class);
                orderDbMap.putAll(inputMap);
                var order = objectMapper.convertValue(orderDbMap, PharmacyOrder.class);
                repository.save(order);
                break;
            case PATIENT:
                var patDbMap = objectMapper.convertValue(pharmacyOrder.getPatient(), Map.class);
                patDbMap.putAll(inputMap);
                var patient = objectMapper.convertValue(patDbMap, Patient.class);
                pharmacyOrder.setPatient(patient);
                repository.save(pharmacyOrder);
                break;
            case ENCOUNTER:
                var encounterDbMap = objectMapper.convertValue(pharmacyOrder.getEncounter(), Map.class);
                encounterDbMap.putAll(inputMap);
                var encounter = objectMapper.convertValue(encounterDbMap, Encounter.class);
                pharmacyOrder.setEncounter(encounter);
                repository.save(pharmacyOrder);
            case ITEM:
                List<Items> items = (List<Items>) inputMap.get("items");
                pharmacyOrder.setItems(items);
                repository.save(pharmacyOrder);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //response generate
        //Get the updated latest data refreshing here
        pharmacyOrder = getPharmacyOrder(orderId);
        PharmacyOrderDto response = mapper.map(pharmacyOrder, PharmacyOrderDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(pharmacyOrder);
        response.setAudit(auditDto);

        return response;
    }

    public PharmacyOrder getPharmacyOrder(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy Order ::" + id + " not found"));
    }

    public PageResponse filterPharmacyOrder(LocalDate stDate, LocalDate edDate, String patientName,
                                            String uhId, String statuses, String prescriptionId, int page, int size) {
        List<Criteria> criteria = new ArrayList<>();
        if (stDate != null)
            criteria.add(Criteria.where("orderDate").gte(stDate));
        if (edDate != null)
            criteria.add(Criteria.where("orderDate").lte(edDate));
        if (patientName != null)
            criteria.add(Criteria.where(("patient.name")).is(patientName));
        if (prescriptionId != null)
            criteria.add(Criteria.where(("encounter.prescriptionId")).is(prescriptionId));
        if (uhId != null)
            criteria.add(Criteria.where("patient.uhId").is(uhId));
        if (statuses != null)
            criteria.add(Criteria.where("status").in(Stream.of(statuses.split(","))
                    .collect(Collectors.toList())));
        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if (!criteria.isEmpty())
            return buildPageResponse(repository.query(searchQuery, paging));
        return buildPageResponse(repository.findAll(paging));
    }

    private PageResponse buildPageResponse(Page<PharmacyOrder> pharmacyOrder) {
        if (pharmacyOrder == null || pharmacyOrder.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + pharmacyOrder.getTotalElements());
            Page<PharmacyOrderDto> dtos = buildPharmacyOrderDtos(pharmacyOrder);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) pharmacyOrder.getTotalPages())
                            .totalElements(pharmacyOrder.getTotalElements())
                            .number(pharmacyOrder.getNumber())
                            .size(pharmacyOrder.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }

    private Page<PharmacyOrderDto> buildPharmacyOrderDtos(Page<PharmacyOrder> pharmacyOrder) {
        return pharmacyOrder.map(p -> {
            // Conversion logic
            PharmacyOrderDto dto = mapper.map(p, PharmacyOrderDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            dto.setAudit(auditDto);
            return dto;
        });
    }

    private AuditDto buildAuditDto(PharmacyOrder pharmacyOrder) {
        return AuditDto.builder().createdBy(pharmacyOrder.getCreatedBy())
                .modifiedBy(pharmacyOrder.getModifiedBy())
                .dateCreated(pharmacyOrder.getDateCreated())
                .dateModified(pharmacyOrder.getDateModified())
                .build();
    }
}
