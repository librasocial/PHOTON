package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.dtos.AuditDTO;
import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.OrderSamplesDTO;
import com.ssf.laborders.dtos.OrderSamplesPatchDTO;
import com.ssf.laborders.entities.OrderSamples;
import com.ssf.laborders.entities.Sample;
import com.ssf.laborders.repository.OrderSamplesRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OrderSamplesService implements IOrderSamplesService {

    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private OrderSamplesRepository orderSamplesRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public LabOrdersPageResponse createOrderSamples(OrderSamplesDTO orderSamplesDTO) {
        OrderSamples orderSamples = mapper.map(orderSamplesDTO, OrderSamples.class);
        orderSamples.setAudit(getCreatedByAuditor());
        orderSamples = orderSamplesRepository.save(orderSamples);
        log.info("OrderSamples Created :: {}", orderSamples.getId());
        return LabOrdersPageResponse.builder().totalPages(1L).content(Arrays.asList(orderSamples)).totalElements(1L).build();
    }

    @Override
    public LabOrdersPageResponse getOrderSamples(String orderId, String sampleId) {
        List<OrderSamples> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<OrderSamples> orderSamplesOptional = orderSamplesRepository.findByOrderIdAndId(orderId, sampleId);
        if (orderSamplesOptional.isPresent()) {
            totalPages = 1L;
            totalElements = Long.valueOf(results.size());
            results.add(orderSamplesOptional.get());
        }
        return LabOrdersPageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();
    }

    @Override
    public LabOrdersPageResponse updateOrderSamples(String orderId, String sampleId, OrderSamplesPatchDTO samplesPatchDTO) {
        Optional<OrderSamples> orderOptional = orderSamplesRepository.findByOrderIdAndId(orderId, sampleId);
        OrderSamples orderSamples;
        if (orderOptional.isPresent()) {
            orderSamples = orderOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        orderSamples.setAudit(getModifiedByAuditor(orderSamples.getAudit()));
        Map<String, Object> inputMap = samplesPatchDTO.getProperties();
        switch (samplesPatchDTO.getType()) {
            case OrderSamples:
                Map<String, Object> orderSamplesMap = objectMapper.convertValue(orderSamples, Map.class);
                orderSamplesMap.putAll(inputMap);
                OrderSamples orderSample = objectMapper.convertValue(orderSamplesMap, OrderSamples.class);
                orderSamples = orderSamplesRepository.save(orderSample);
                break;
            case Sample:
                Map<String, Object> sampleMap = objectMapper.convertValue(orderSamples.getSample(), Map.class);
                sampleMap.putAll(inputMap);
                Sample sample = objectMapper.convertValue(sampleMap, Sample.class);
                orderSamples.setSample(sample);
                orderSamples = orderSamplesRepository.save(orderSamples);
                break;
        }
        log.info("OrderSamples Updated :: {}", orderSamples.getId());
        return LabOrdersPageResponse.builder().totalPages(1L).content(Arrays.asList(orderSamples)).totalElements(1L).build();
    }

    @Override
    public LabOrdersPageResponse getOrderSamplesByFilter(String orderId, Integer page, Integer size) {
        Page<OrderSamples> orderSamplesPage = orderSamplesRepository.findAllByOrderId(orderId, PageRequest.of(page, size));
        return LabOrdersPageResponse.builder().totalPages(Long.valueOf(orderSamplesPage.getTotalPages())).totalElements(orderSamplesPage.getTotalElements()).content(orderSamplesPage.getContent()).build();
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
