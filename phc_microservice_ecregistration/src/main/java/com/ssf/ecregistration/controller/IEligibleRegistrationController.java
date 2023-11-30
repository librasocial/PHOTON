package com.ssf.ecregistration.controller;

import com.ssf.ecregistration.dtos.EligibleRegPageResponse;
import com.ssf.ecregistration.dtos.EligibleRegPatchDto;
import com.ssf.ecregistration.dtos.EligibleRegistrationDto;
import com.ssf.ecregistration.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface IEligibleRegistrationController {

    @Operation(summary = "Creates new ECRegistration.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public EligibleRegistrationDto registration(@Valid @RequestBody EligibleRegistrationDto registrationDto);

    @Operation(summary = "Updates or Sets a ECRegistration", responses = {
            @ApiResponse(responseCode = "202", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/{registrationId}")
    public EligibleRegistrationDto registrationPatch(@PathVariable String registrationId, @RequestBody EligibleRegPatchDto request);

    @Operation(summary = "Retrieves a ECRegistration", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{registrationId}")
    public EligibleRegistrationDto getECRegistrationById(@PathVariable String registrationId);

    @Operation(summary = "Retrieves ECRegistrations based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Un-authorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/filter")
    public EligibleRegPageResponse filterEcReg(@RequestParam(value = "rchId", required = false) String rchId,
                                               @RequestParam(value = "regId", required = false) String regId,
                                               @RequestParam(value = "dataEntryStatus", required = false) String dataEntryStatus,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size);
}
