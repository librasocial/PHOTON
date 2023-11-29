package com.ssf.immunization.controller;

import com.ssf.immunization.dtos.FilterDto;
import com.ssf.immunization.dtos.ImmunizationDto;
import com.ssf.immunization.dtos.PageResponse;
import com.ssf.immunization.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IImmunizationController {

    @Operation(summary = "Creates new Immunization.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ImmunizationDto createImmun(@Valid @RequestBody ImmunizationDto request);

    @Operation(summary = "Retrieves a Immunization", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{immunizationId}")
    public ImmunizationDto readImmun(@PathVariable("immunizationId") String id);

    @Operation(summary = "Filter Immunizations based on filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(value = "/filter")
    public PageResponse getImmunByFilter(@RequestBody FilterDto filterDto);

    @Operation(summary = "Updates or Sets a Immunization's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{immunizationId}")
    public ImmunizationDto patchImmun(@PathVariable("immunizationId") String id, @RequestBody  Map<String, Object> request);


}
