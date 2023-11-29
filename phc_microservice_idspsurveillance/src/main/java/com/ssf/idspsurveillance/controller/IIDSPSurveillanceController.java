package com.ssf.idspsurveillance.controller;

import com.ssf.idspsurveillance.dtos.IDSPSurveillanceDto;
import com.ssf.idspsurveillance.dtos.PageResponse;
import com.ssf.idspsurveillance.dtos.PatchDto;
import com.ssf.idspsurveillance.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

public interface IIDSPSurveillanceController {

    @Operation(summary = "Creates new IDSPSurveillance.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    IDSPSurveillanceDto create(@Valid @RequestBody IDSPSurveillanceDto request);

    @Operation(summary = "Retrieves a IDSPSurveillance", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{surveillanceId}")
    IDSPSurveillanceDto read(@PathVariable("surveillanceId") String id);

    @Operation(summary = "Updates or Sets a IDSPSurveillance's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{surveillanceId}")
    IDSPSurveillanceDto patch(@PathVariable("surveillanceId") String id,
                              @RequestBody PatchDto patchDto);

    @Operation(summary = "Retrieves IDSPSurveillance based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    PageResponse filter(@RequestParam(value = "citizenId", required = false) String citizenId,
                        @RequestParam(value = "stDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate stDate,
                        @RequestParam(value = "edDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate edDate,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size);
}
