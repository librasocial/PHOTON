package com.ssf.laborders.controller;

import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.TestResultsDTO;
import com.ssf.laborders.dtos.TestResultsPatchDTO;
import com.ssf.laborders.service.ITestResultsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laborders")
public class TestResultsController {

    @Autowired
    private ITestResultsService service;

    @Operation(summary = "Create TestResults by orderId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{orderId}/results")
    public ResponseEntity<LabOrdersPageResponse> createLabOrder(@RequestBody TestResultsDTO testResultsDTO) {
        return ResponseEntity.ok(service.createTestResults(testResultsDTO));
    }

    @Operation(summary = "Get TestResults by OrderId and SampleId ", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{orderId}/results/{resultId}")
    public ResponseEntity<LabOrdersPageResponse> getTestResults(@PathVariable String orderId,
                                                                @PathVariable String resultId) {
        return ResponseEntity.ok(service.getTestResults(orderId, resultId));
    }

    @Operation(summary = "Update TestResults", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{orderId}/results/{resultId}")
    public ResponseEntity<LabOrdersPageResponse> updateTestResults(@PathVariable String orderId,
                                                                   @PathVariable String resultId,
                                                                   @RequestBody TestResultsPatchDTO resultsPatchDTO) {
        return ResponseEntity.ok(service.updateTestResults(orderId, resultId, resultsPatchDTO));
    }

    @Operation(summary = "Get all TestResults by OrderId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{orderId}/results/filter")
    public ResponseEntity<LabOrdersPageResponse> getTestResultsByFilter(@PathVariable String orderId,
                                                                        @RequestParam(defaultValue = "0") Integer page,
                                                                        @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(service.getTestResultsByFilter(orderId, page, size));
    }

}
