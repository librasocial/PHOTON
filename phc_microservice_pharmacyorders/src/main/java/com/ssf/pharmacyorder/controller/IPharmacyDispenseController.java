package com.ssf.pharmacyorder.controller;

import com.ssf.pharmacyorder.dtos.DispenseDto;
import com.ssf.pharmacyorder.dtos.PageResponse;
import com.ssf.pharmacyorder.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

public interface IPharmacyDispenseController {

    @Operation(summary = "Creates new PharmacyOrder's Dispensement.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{orderId}/dispenses")
    public DispenseDto createDispense(@PathVariable("orderId") String orderId, @Valid @RequestBody DispenseDto request);

    @Operation(summary = "Retrieves a PharmacyOrder's Dispensement", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{orderId}/dispenses/{dispenseId}")
    public DispenseDto readDispense(@PathVariable("orderId") String orderId, @PathVariable("dispenseId") String dispenseId);

    @Operation(summary = "Updates or Sets a PharmacyOrder's Dispense properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{orderId}/dispenses/{dispenseId}")
    public DispenseDto pharmacyDispensePatch(@PathVariable("orderId") String orderId, @PathVariable("dispenseId") String dispenseId,
                                             @RequestBody Map<String, Object> patchDto);

    @Operation(summary = "Retrieves PharmacyOrders Dispensement based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/dispenses/filter")
    public PageResponse filterPharmacyDispense(@RequestParam(value = "stDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate stDate,
                                               @RequestParam(value = "edDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate edDate,
                                               @RequestParam(value = "orderId", required = false) String orderId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size);

}
