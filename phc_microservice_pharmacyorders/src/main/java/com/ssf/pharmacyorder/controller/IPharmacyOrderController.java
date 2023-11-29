package com.ssf.pharmacyorder.controller;

import com.ssf.pharmacyorder.dtos.PageResponse;
import com.ssf.pharmacyorder.dtos.PharmacyOrderDto;
import com.ssf.pharmacyorder.dtos.PharmacyOrderPatchDto;
import com.ssf.pharmacyorder.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

public interface IPharmacyOrderController {

    @Operation(summary = "Creates new PharmacyOrders.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public PharmacyOrderDto createPharmacyOrder(@Valid @RequestBody PharmacyOrderDto request);

    @Operation(summary = "Retrieves a PharmacyOrder", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{orderId}")
    public PharmacyOrderDto readPharmacyOrder(@PathVariable("orderId") String id);

    @Operation(summary = "Updates or Sets a PharmacyOrder's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{orderId}")
    public PharmacyOrderDto patchPharmacyOrder(@PathVariable("orderId") String id,
                                               @RequestBody PharmacyOrderPatchDto patchDto);

    @Operation(summary = "Retrieves PharmacyOrders based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    public PageResponse filterPharmacyOrder(@RequestParam(value = "stDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate stDate,
                                            @RequestParam(value = "edDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate edDate,
                                            @RequestParam(value = "patientName", required = false) String patientName,
                                            @RequestParam(value = "UHID", required = false) String uhId,
                                            @RequestParam(value = "statuses", required = false) String statuses,
                                            @RequestParam(value = "prescriptionId", required = false) String prescriptionId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size);

}
