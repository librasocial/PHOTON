package com.ssf.laborders.controller;

import com.ssf.laborders.dtos.LabOrderDTO;
import com.ssf.laborders.dtos.LabOrderPatchDTO;
import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.service.ILabOrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laborders")
@Slf4j
public class LabOrdersController {

    @Autowired
    private ILabOrdersService service;

    @GetMapping("/health")
    public String getHealth() {
        return service.getHealth();
    }

    @Operation(summary = "Create LabOrder", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("")
    public ResponseEntity<LabOrdersPageResponse> createLabOrder(@RequestBody LabOrderDTO labOrderDTO) {
        return ResponseEntity.ok(service.createLabOrder(labOrderDTO));
    }

    @Operation(summary = "Get LabOrder by Order id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<LabOrdersPageResponse> getLabOrders(@PathVariable String orderId) {
        return ResponseEntity.ok(service.getLabOrders(orderId));
    }

    @Operation(summary = "Update LabOrder", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{orderId}")
    public ResponseEntity<LabOrdersPageResponse> updateLabOrders(@PathVariable String orderId, @RequestBody LabOrderPatchDTO labOrderPatchDto) {
        return ResponseEntity.ok(service.updateLabOrders(orderId, labOrderPatchDto));
    }

    @Operation(summary = "Get LabOrder by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/filter")
    public ResponseEntity<LabOrdersPageResponse> getLabOrdersByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "5") Integer size,
                                                                      @RequestParam(required = false) String startDate,
                                                                      @RequestParam(required = false) String endDate,
                                                                      @RequestParam(required = false) String uhId,
                                                                      @RequestParam(required = false) String careContext,
                                                                      @RequestParam(required = false) String encounterId,
                                                                      @RequestParam(required = false) String statuses) {
        return ResponseEntity.ok(service.getLabOrdersByFilter(startDate, endDate,careContext, uhId, encounterId, statuses, page, size));
    }


}
