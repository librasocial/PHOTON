package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.dtos.AuditDTO;
import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.TestResultsDTO;
import com.ssf.laborders.dtos.TestResultsPatchDTO;
import com.ssf.laborders.entities.Sample;
import com.ssf.laborders.entities.TestResults;
import com.ssf.laborders.repository.TestResultsRepository;
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
public class TestResultsService implements ITestResultsService {

    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    private TestResultsRepository testResultsRepository;

    @Autowired
    private AuditorAware<String> auditorProvider;

    @Autowired
    private ModelMapper mapper;

    @Override
    public LabOrdersPageResponse createTestResults(TestResultsDTO testResultsDTO) {
        TestResults testResults = mapper.map(testResultsDTO, TestResults.class);
        testResults.setAudit(getCreatedByAuditor());
        testResults = testResultsRepository.save(testResults);
        log.info("Test Results Created :: {}", testResults.getId());
        return LabOrdersPageResponse.builder().totalPages(1L).content(Arrays.asList(testResults)).totalElements(1L).build();
    }

    @Override
    public LabOrdersPageResponse getTestResults(String orderId, String resultId) {
        List<TestResults> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<TestResults> testResultsOptional = testResultsRepository.findByOrderIdAndId(orderId, resultId);
        if (testResultsOptional.isPresent()) {
            totalPages = 1L;
            totalElements = Long.valueOf(results.size());
            results.add(testResultsOptional.get());
        }
        return LabOrdersPageResponse.builder().totalPages(totalPages).totalElements(totalElements)
                .content(results).build();
    }

    @Override
    public LabOrdersPageResponse updateTestResults(String orderId, String resultId, TestResultsPatchDTO resultsPatchDTO) {
        Optional<TestResults> testResultsOptional = testResultsRepository.findByOrderIdAndId(orderId, resultId);
        TestResults testResult;
        if (testResultsOptional.isPresent()) {
            testResult = testResultsOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        testResult.setAudit(getModifiedByAuditor(testResult.getAudit()));
        Map<String, Object> inputMap = resultsPatchDTO.getProperties();
        switch (resultsPatchDTO.getType()) {
            case TestResults:
                Map<String, Object> testResultsMap = objectMapper.convertValue(testResult, Map.class);
                testResultsMap.putAll(inputMap);
                TestResults testResults = objectMapper.convertValue(testResultsMap, TestResults.class);
                testResult = testResultsRepository.save(testResults);
                break;
            case Sample:
                Map<String, Object> sampleMap = objectMapper.convertValue(testResult.getSample(), Map.class);
                sampleMap.putAll(inputMap);
                Sample sample = objectMapper.convertValue(sampleMap, Sample.class);
                testResult.setSample(sample);
                testResult = testResultsRepository.save(testResult);
                break;
        }
        log.info("TestResults Updated :: {}", testResult.getId());
        return LabOrdersPageResponse.builder().totalPages(1L).content(Arrays.asList(testResult)).totalElements(1L).build();
    }

    @Override
    public LabOrdersPageResponse getTestResultsByFilter(String orderId, Integer page, Integer size) {
        Page<TestResults> testResultsPage = testResultsRepository.findAllByOrderId(orderId, PageRequest.of(page, size));
        return LabOrdersPageResponse.builder().totalPages(Long.valueOf(testResultsPage.getTotalPages())).totalElements(testResultsPage.getTotalElements()).content(testResultsPage.getContent()).build();
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
