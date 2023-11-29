package com.ssf.purchaseorder.controller;

import com.ssf.purchaseorder.dtos.PageResponse;
import com.ssf.purchaseorder.dtos.PurchaseOrderDto;
import com.ssf.purchaseorder.dtos.PurchaseOrderPatchDto;
import com.ssf.purchaseorder.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

public interface IPurchaseOrderController {
    @Operation(summary = "Creates new PurchaseOrders.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public PurchaseOrderDto createPurchaseOrder(@Valid @RequestBody PurchaseOrderDto request);

    @Operation(summary = "Retrieves a PurchaseOrder", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{orderId}")
    public PurchaseOrderDto readPurchaseOrder(@PathVariable("orderId") String id);

    @Operation(summary = "Updates or Sets a PurchaseOrder's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{orderId}")
    public PurchaseOrderDto patchPurchaseOrder(@PathVariable("orderId") String id,
                                               @RequestBody PurchaseOrderPatchDto patchDto);

    @Operation(summary = "Retrieves PurchaseOrders based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    public PageResponse filterPurchaseOrder(@RequestParam(value= "stDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate stDate,
                                            @RequestParam(value= "edDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate edDate,
                                            @RequestParam(value = "poType", required = false) String poType,
                                            @RequestParam(value = "supplierName", required = false) String supplierName,
                                            @RequestParam(value = "statuses", required = false) String statuses,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size);
}
