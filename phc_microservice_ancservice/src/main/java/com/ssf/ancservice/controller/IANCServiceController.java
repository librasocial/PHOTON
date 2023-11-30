package com.ssf.ancservice.controller;

import com.ssf.ancservice.constant.DataEntryStatusEnum;
import com.ssf.ancservice.dtos.ANCPageResponse;
import com.ssf.ancservice.dtos.ANCServiceDto;
import com.ssf.ancservice.dtos.ANCServicePatchDto;
import com.ssf.ancservice.entities.ANCService;
import com.ssf.ancservice.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

public interface IANCServiceController {

    @Operation(summary = "Creates new ANCService.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ANCServiceDto createANC(@Valid @RequestBody ANCServiceDto registrationDto);

    @Operation(summary = "Updates ANCService", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{serviceId}")
    public ANCServiceDto patchANC(@PathVariable("serviceId") String serviceId,
                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                          schema = @Schema(implementation = ANCServicePatchDto.class)))
                                  @RequestBody ANCServicePatchDto patchDto);

    @Operation(summary = "Get ANCService", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{serviceId}")
    public ANCService getANC(@PathVariable("serviceId") String serviceId);

    @Operation(summary = "Get ANCService by Filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/filter")
    public ANCPageResponse getANCByFilter(@RequestParam("rchId") Optional<String> rchId,
                                          @RequestParam("serviceId") Optional<String> serviceId,
                                          @RequestParam("dataEntryStatus") Optional<DataEntryStatusEnum> dataEntryStatus,
                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "5") Integer size);


}
