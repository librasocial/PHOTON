package com.ssf.purchaseorder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.purchaseorder.dtos.*;
import com.ssf.purchaseorder.entities.IndentItem;
import com.ssf.purchaseorder.entities.PoDetails;
import com.ssf.purchaseorder.entities.PoItem;
import com.ssf.purchaseorder.entities.PurchaseOrder;
import com.ssf.purchaseorder.exception.EntityNotFoundException;
import com.ssf.purchaseorder.repository.IPurchaseOrderRepository;
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
public class PurchaseOrderService {
    @Autowired
    private IPurchaseOrderRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto request) {
        log.debug("Creating the purchaseOrder");

        PurchaseOrder purchaseOrder = mapper.map(request, PurchaseOrder.class);
        repository.save(purchaseOrder);
        PurchaseOrderDto response = mapper.map(purchaseOrder, PurchaseOrderDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(purchaseOrder);
        response.setAudit(auditDto);
        return response;
    }

    public PurchaseOrderDto readPurchaseOrder(String id) {
        log.debug("Reading the PurchaseOrder for purchaseOrderId {}", id);
        auditorProvider.getCurrentAuditor();
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        PurchaseOrderDto response = mapper.map(purchaseOrder, PurchaseOrderDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(purchaseOrder);
        response.setAudit(auditDto);

        return response;

    }
    public PurchaseOrderDto purchaseOrderPatch(String orderId, PurchaseOrderPatchDto request) {
        log.debug("Partial Edit for PurchaseOrder orderId  {} ", orderId);
        var purchaseOrder = getPurchaseOrder(orderId);
        var inputMap = request.getProperties();
        switch (request.getType()) {
            case PO:
                var orderDbMap = objectMapper.convertValue(purchaseOrder, Map.class);
                orderDbMap.putAll(inputMap);
                var order = objectMapper.convertValue(orderDbMap, PurchaseOrder.class);
                repository.save(order);
                break;
            case PODETAILS:
                if(inputMap.containsKey("poItems")) {
                    List<PoItem> items = (List<PoItem>) inputMap.get("items");
                    purchaseOrder.getPoDetails().setPoItems(items);
                }
                var poDetailsDbMap = objectMapper.convertValue(purchaseOrder.getPoDetails(), Map.class);
                poDetailsDbMap.putAll(inputMap);
                var poDetails = objectMapper.convertValue(poDetailsDbMap, PoDetails.class);
                purchaseOrder.setPoDetails(poDetails);
                repository.save(purchaseOrder);
                break;
            case INDENTITEMS:
                List<IndentItem> items = (List<IndentItem>) inputMap.get("indentItems");
                purchaseOrder.setIndentItems(items);
                repository.save(purchaseOrder);
                break;
            default:
                throw new RuntimeException("Invalid Request Type");
        }
        //response generate
        //Get the updated latest data refreshing here
        purchaseOrder= getPurchaseOrder(orderId);
        PurchaseOrderDto response = mapper.map(purchaseOrder, PurchaseOrderDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(purchaseOrder);
        response.setAudit(auditDto);

        return response;
    }

    public PurchaseOrder getPurchaseOrder(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PurchaseOrder ::" + id + " not found"));
    }

    public PageResponse filterPurchaseOrder(LocalDate stDate, LocalDate edDate, String poType,
                                            String supplierName, String statuses, int page, int size) {
        List<Criteria> criteria = new ArrayList<>();
        if(stDate != null )
            criteria.add(Criteria.where("poDate").gte(stDate));
        if(edDate !=  null)
            criteria.add(Criteria.where("poDate").lte(edDate));
        if(poType!=null && poType.equalsIgnoreCase("PO"))
            criteria.add(Criteria.where(("indentItems")).isNull());
        if(poType!=null && poType.equalsIgnoreCase("Indent"))
            criteria.add(Criteria.where(("poDetails")).isNull());
        if(supplierName !=null)
            criteria.add(Criteria.where("supplierName").in(Stream.of(supplierName.split(","))
                    .collect(Collectors.toList())));
        if(statuses!=null)
            criteria.add(Criteria.where("status").in(Stream.of(statuses.split(","))
                    .collect(Collectors.toList())));
        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if(!criteria.isEmpty())
            return  buildPageResponse(repository.query(searchQuery, paging));
        return buildPageResponse(repository.findAll(paging));
    }

    private PageResponse buildPageResponse(Page<PurchaseOrder> purchaseOrder) {
        if (purchaseOrder == null || purchaseOrder.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + purchaseOrder.getTotalElements());
            Page<PurchaseOrderDto> dtos = buildResponseDtos(purchaseOrder);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) purchaseOrder.getTotalPages())
                            .totalElements(purchaseOrder.getTotalElements())
                            .number(purchaseOrder.getNumber())
                            .size(purchaseOrder.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }
    private Page<PurchaseOrderDto> buildResponseDtos(Page<PurchaseOrder> purchaseOrder) {
        return purchaseOrder.map(p -> {
            // Conversion logic
            PurchaseOrderDto dto = mapper.map(p, PurchaseOrderDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            dto.setAudit(auditDto);
            return dto;
        });
    }
    private AuditDto buildAuditDto(PurchaseOrder purchaseOrder) {
        return AuditDto.builder().createdBy(purchaseOrder.getCreatedBy())
                .modifiedBy(purchaseOrder.getModifiedBy())
                .dateCreated(purchaseOrder.getDateCreated())
                .dateModified(purchaseOrder.getDateModified())
                .build();
    }
}
