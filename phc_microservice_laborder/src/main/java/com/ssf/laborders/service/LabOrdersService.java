package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.dtos.AuditDTO;
import com.ssf.laborders.dtos.LabOrderDTO;
import com.ssf.laborders.dtos.LabOrderPatchDTO;
import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.entities.Encounter;
import com.ssf.laborders.entities.LabOrder;
import com.ssf.laborders.entities.Patient;
import com.ssf.laborders.repository.LabOrdersRepository;
import com.ssf.laborders.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class LabOrdersService implements ILabOrdersService {
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
    @Autowired
    private LabOrdersRepository labOrdersRepository;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private IOrdersSummaryService summaryService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public String getHealth() {
        try {
            labOrdersRepository.count();
            return "Lab Orders Service Up and Running";
        } catch (Exception e) {
            log.error("DB connection issue please check.");
            return "Lab Orders Service Issue connecting DB.";
        }
    }

    @Override
    public LabOrdersPageResponse createLabOrder(LabOrderDTO labOrderDTO) {
        LabOrder labOrder = mapper.map(labOrderDTO, LabOrder.class);
        labOrder.setCareContext(ObjectId.get().toHexString());
        labOrder.setAudit(getCreatedByAuditor());
        labOrder = labOrdersRepository.save(labOrder);
        summaryService.updateOrdersSummary(labOrder);
        log.info("LabOrder Created :: {}", labOrder.getId());
        return LabOrdersPageResponse.builder().totalPages(1L).content(Arrays.asList(labOrder)).totalElements(1L).build();
    }

    @Override
    public LabOrdersPageResponse getLabOrders(String orderId) {
        List<LabOrder> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<LabOrder> labOrderOptional = labOrdersRepository.findById(orderId);
        if (labOrderOptional.isPresent()) {
            totalPages = 1L;
            totalElements = Long.valueOf(results.size());
            results.add(labOrderOptional.get());
        }
        return LabOrdersPageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();
    }

    @Override
    public LabOrdersPageResponse updateLabOrders(String orderId, LabOrderPatchDTO labOrderPatchDto) {
        Optional<LabOrder> orderOptional = labOrdersRepository.findById(orderId);
        LabOrder order;
        if (orderOptional.isPresent()) {
            order = orderOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        order.setAudit(getModifiedByAuditor(order.getAudit()));
        Map<String, Object> inputMap = labOrderPatchDto.getProperties();
        switch (labOrderPatchDto.getType()) {
            case LabOrders:
                Map<String, Object> labOrderMap = objectMapper.convertValue(order, Map.class);
                labOrderMap.putAll(inputMap);
                LabOrder labOrder = objectMapper.convertValue(labOrderMap, LabOrder.class);
                order = labOrdersRepository.save(labOrder);
                break;
            case Patient:
                Map<String, Object> patientMap = objectMapper.convertValue(order.getPatient(), Map.class);
                patientMap.putAll(inputMap);
                Patient patient = objectMapper.convertValue(patientMap, Patient.class);
                order.setPatient(patient);
                order = labOrdersRepository.save(order);
                break;
            case Encounter:
                Map<String, Object> encounterMap = objectMapper.convertValue(order.getEncounter(), Map.class);
                encounterMap.putAll(inputMap);
                Encounter encounter = objectMapper.convertValue(encounterMap, Encounter.class);
                order.setEncounter(encounter);
                order = labOrdersRepository.save(order);
                break;
        }
        summaryService.updateOrdersSummary(order);
        log.info("LabOrder Updated :: {}", order.getId());
        return LabOrdersPageResponse.builder().totalPages(1L).content(Arrays.asList(order)).totalElements(1L).build();
    }

    @Override
    public LabOrdersPageResponse getLabOrdersByFilter(String startDate, String endDate, String careContext, String uhId, String encounterId, String statuses, Integer page, Integer size) {
        Page<LabOrder> labOrderPage;
        List<Criteria> criteria = new ArrayList<>();
        if (startDate != null && endDate != null) {
            startDate = startDate + "T00:00:00.000Z";
            endDate = endDate + "T23:59:59.000Z";
            criteria.add(Criteria.where("orderDate")
                    .gte(Utils.stringToLocalDateTime(startDate))
                    .lte(Utils.stringToLocalDateTime(endDate)));
        }
        if (encounterId != null) {
            criteria.add(Criteria.where(("encounter.encounterId")).is(encounterId));
        }
        if (uhId != null) {
            criteria.add(Criteria.where("patient.uhid").is(uhId));
        }
        if (careContext != null) {
            criteria.add(Criteria.where("careContext").is(careContext));
        }
        if (statuses != null) {
            criteria.add(Criteria.where("status").in(Stream.of(statuses.split(","))
                    .collect(Collectors.toList())));
        }
        Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
        Query searchQuery = new Query(criteriaQuery);
        Pageable paging = PageRequest.of(page, size);
        if (!criteria.isEmpty()) {
            labOrderPage = labOrdersRepository.queryLabOrders(searchQuery, paging);
        } else {
            labOrderPage = labOrdersRepository.findAll(paging);
        }
        return LabOrdersPageResponse.builder().totalPages(Long.valueOf(labOrderPage.getTotalPages())).totalElements(labOrderPage.getTotalElements()).content(labOrderPage.getContent()).build();
    }

    private AuditDTO getCreatedByAuditor() {
        String username = "";
        Optional<String> optional = auditorProvider.getCurrentAuditor();
        if (optional.isPresent()) {
            username = optional.get();
        }
        return AuditDTO.builder().modifiedBy(username).createdBy(username).build();
    }

    private AuditDTO getModifiedByAuditor(AuditDTO auditDTO) {
        String username = "";
        Optional<String> optional = auditorProvider.getCurrentAuditor();
        if (optional.isPresent()) {
            username = optional.get();
        }
        auditDTO.setModifiedBy(username);
        return auditDTO;
    }

}
