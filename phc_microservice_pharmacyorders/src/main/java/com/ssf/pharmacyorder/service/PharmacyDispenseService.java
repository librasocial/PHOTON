package com.ssf.pharmacyorder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pharmacyorder.dtos.*;
import com.ssf.pharmacyorder.entities.Dispense;
import com.ssf.pharmacyorder.exception.EntityNotFoundException;
import com.ssf.pharmacyorder.repository.IPharmacyDispenseRepository;
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

@Service
@Slf4j
public class PharmacyDispenseService {
    @Autowired
    private IPharmacyOrderRepository orderRepository;
    @Autowired
    private IPharmacyDispenseRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    public DispenseDto createDispense(String orderId, DispenseDto request) {
        log.debug("Creating the dispense");
        if (!orderRepository.existsById(orderId))
            throw new EntityNotFoundException("OrderId not found " + orderId);
        Dispense dispense = mapper.map(request, Dispense.class);
        repository.save(dispense);
        DispenseDto response = mapper.map(dispense, DispenseDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(dispense);
        response.setAudit(auditDto);
        return response;
    }

    public DispenseDto readDispense(String orderId, String id) {
        log.debug("Reading the Dispense for id {} and orderId {}", id, orderId);
        auditorProvider.getCurrentAuditor();
        Dispense dispense = getPharmacyDispense(orderId, id);
        DispenseDto response = mapper.map(dispense, DispenseDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(dispense);
        response.setAudit(auditDto);

        return response;

    }

    public DispenseDto pharmacyDispensePatch(String orderId, String id, Map<String, Object> request) {
        log.debug("Partial Edit for Dispense orderId  {} ", orderId);
        if (!orderRepository.existsById(orderId))
            throw new EntityNotFoundException("OrderId not found " + orderId);
        DispenseDto dispenseDto = objectMapper.convertValue(request, DispenseDto.class);

        Dispense existingProduct = getPharmacyDispense(orderId, id);
        Map<String, Object> dbMap = objectMapper.convertValue(existingProduct, Map.class);

        if (request.containsKey("items")) {
            List<DispenseItemsDto> newItems = (List<DispenseItemsDto>) request.get("umo");
            existingProduct.setItems(newItems);
        }
        dbMap.putAll(request);
        Dispense data = objectMapper.convertValue(dbMap, Dispense.class);
        repository.save(data);

        //response generate
        var response = mapper.map(data, DispenseDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(existingProduct);
        response.setAudit(auditDto);
        return response;
    }

    public Dispense getPharmacyDispense(String orderId, String id) {
        return repository.findByIdAndOrderId(id, orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pharmacy Dispense ::" + id + " not found"));
    }

    public PageResponse filterPharmacyDispense(LocalDate stDate, LocalDate edDate, String orderId,
                                            int page, int size) {
        List<Criteria> criteria = new ArrayList<>();
        if (stDate != null)
            criteria.add(Criteria.where("orderDate").gte(stDate));
        if (edDate != null)
            criteria.add(Criteria.where("orderDate").lte(edDate));
        if (orderId != null)
            criteria.add(Criteria.where(("orderId")).is(orderId));

        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if (!criteria.isEmpty())
            return buildPageResponse(repository.queryDispense(searchQuery, paging));
        return buildPageResponse(repository.findAll(paging));
    }

    private PageResponse buildPageResponse(Page<Dispense> dispense) {
        if (dispense == null || dispense.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + dispense.getTotalElements());
            Page<DispenseDto> dtos = buildPharmacyDispenseDtos(dispense);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) dispense.getTotalPages())
                            .totalElements(dispense.getTotalElements())
                            .number(dispense.getNumber())
                            .size(dispense.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }

    private Page<DispenseDto> buildPharmacyDispenseDtos(Page<Dispense> dispense) {
        return dispense.map(p -> {
            // Conversion logic
            DispenseDto dto = mapper.map(p, DispenseDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            dto.setAudit(auditDto);
            return dto;
        });
    }

    private AuditDto buildAuditDto(Dispense dispense) {
        return AuditDto.builder().createdBy(dispense.getCreatedBy())
                .modifiedBy(dispense.getModifiedBy())
                .dateCreated(dispense.getDateCreated())
                .dateModified(dispense.getDateModified())
                .build();
    }
}
