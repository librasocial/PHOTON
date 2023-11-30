package com.ssf.laborders.controller;

import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.service.IOrdersSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/laborders")
@Slf4j
public class OrdersSummaryController {


    @Autowired
    IOrdersSummaryService service;

    @Operation(summary = "Get OrdersSummary by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/summary/filter")
    public ResponseEntity<LabOrdersPageResponse> getLabOrdersByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "5") Integer size,
                                                                      @RequestParam Optional<String> startDate,
                                                                      @RequestParam Optional<String> endDate) {
        return ResponseEntity.ok(service.getOrdersSummaryByFilter(startDate.orElse(null), endDate.orElse(null), page, size));
    }
}
