package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.PNCPageResponse;
import com.ssf.pncservice.dtos.PNCServiceDto;
import com.ssf.pncservice.dtos.PNCServicePatchDto;
import com.ssf.pncservice.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface IPNCServiceController {

    @Operation(summary = "Creates new PNCService.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public PNCServiceDto createPNC(@Valid @RequestBody PNCServiceDto pncServiceDto);

    @Operation(summary = "Updates or Sets a PNCService", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{serviceId}")
    public PNCServiceDto patchPNC(@PathVariable("serviceId") String serviceId,
                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                          schema = @Schema(implementation = PNCServicePatchDto.class)))
                                  @RequestBody PNCServicePatchDto patchDto);

    @Operation(summary = "Retrieves a PNCService", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{serviceId}")
    public PNCServiceDto getPNC(@PathVariable("serviceId") String serviceId);

    @Operation(summary = "Retrieves PNCServices based on filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/filter")
    public PNCPageResponse getPNCByFilter(@RequestParam(value = "rchId", required = false) String rchId,
                                          @RequestParam(value = "serviceId", required = false) String serviceId,
                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "5") Integer size);

}
