package com.ssf.laborders.controller;

import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.OrderSamplesDTO;
import com.ssf.laborders.dtos.OrderSamplesPatchDTO;
import com.ssf.laborders.service.IOrderSamplesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laborders")
@Slf4j
public class OrderSamplesController {


    @Autowired
    private IOrderSamplesService service;

    @Operation(summary = "Create OrderSamples by orderId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{orderId}/samples")
    public ResponseEntity<LabOrdersPageResponse> createOrderSamples(@RequestBody OrderSamplesDTO orderSamplesDTO) {
        return ResponseEntity.ok(service.createOrderSamples(orderSamplesDTO));
    }

    @Operation(summary = "Get OrderSamples by OrderId and SampleId ", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{orderId}/samples/{sampleId}")
    public ResponseEntity<LabOrdersPageResponse> getOrderSamples(@PathVariable String orderId,
                                                                 @PathVariable String sampleId) {
        return ResponseEntity.ok(service.getOrderSamples(orderId, sampleId));
    }

    @Operation(summary = "Update OrderSamples", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{orderId}/samples/{sampleId}")
    public ResponseEntity<LabOrdersPageResponse> updateOrderSamples(@PathVariable String orderId,
                                                                    @PathVariable String sampleId,
                                                                    @RequestBody OrderSamplesPatchDTO samplesPatchDTO) {
        return ResponseEntity.ok(service.updateOrderSamples(orderId, sampleId, samplesPatchDTO));
    }

    @Operation(summary = "Get all OrderSamples by OrderId", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{orderId}/samples/filter")
    public ResponseEntity<LabOrdersPageResponse> getOrderSamplesByFilter(@PathVariable String orderId,
                                                                         @RequestParam(defaultValue = "0") Integer page,
                                                                         @RequestParam(defaultValue = "5") Integer size) {
        return ResponseEntity.ok(service.getOrderSamplesByFilter(orderId, page, size));
    }


}
