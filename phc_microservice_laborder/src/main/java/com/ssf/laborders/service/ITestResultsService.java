package com.ssf.laborders.service;

import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.TestResultsDTO;
import com.ssf.laborders.dtos.TestResultsPatchDTO;

public interface ITestResultsService {
    LabOrdersPageResponse createTestResults(TestResultsDTO testResultsDTO);

    LabOrdersPageResponse getTestResults(String orderId, String resultId);

    LabOrdersPageResponse updateTestResults(String orderId, String resultId, TestResultsPatchDTO resultsPatchDTO);

    LabOrdersPageResponse getTestResultsByFilter(String orderId, Integer page, Integer size);
}
