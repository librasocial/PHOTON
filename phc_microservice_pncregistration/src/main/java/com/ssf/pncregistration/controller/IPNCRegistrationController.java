package com.ssf.pncregistration.controller;

import com.ssf.pncregistration.dtos.PNCRegPageResponse;
import com.ssf.pncregistration.dtos.PNCRegPatchDto;
import com.ssf.pncregistration.dtos.PNCRegistrationDto;
import com.ssf.pncregistration.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface IPNCRegistrationController {

    @Operation(summary ="Creates new PNCRegistration.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public PNCRegistrationDto registration(@Valid @RequestBody PNCRegistrationDto registrationDto);

    @Operation(summary = "Retrieves a PNCRegistration", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{registrationId}")
    public PNCRegistrationDto readPNC(@PathVariable("registrationId") String registrationId);

    @Operation(summary = "Updates or Sets a PNCRegistration", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{registrationId}")
    public PNCRegistrationDto patchPNC(@PathVariable("registrationId") String registrationId,
                                     @RequestBody PNCRegPatchDto registrationDto);

    @Operation(summary = "Retrieves PNCRegistrations based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    public PNCRegPageResponse filterPNCReg(@RequestParam(value = "rchId", required = false) String rchId,
                                           @RequestParam(value = "regId", required = false) String regId,
                                           @RequestParam(value = "dataEntryStatus", required = false) String dataEntryStatus,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size);
}
