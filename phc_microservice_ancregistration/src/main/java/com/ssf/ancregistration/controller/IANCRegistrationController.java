package com.ssf.ancregistration.controller;

import com.ssf.ancregistration.dtos.ANCRegPageResponse;
import com.ssf.ancregistration.dtos.ANCRegPatchDto;
import com.ssf.ancregistration.dtos.ANCRegistrationDto;
import com.ssf.ancregistration.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IANCRegistrationController {

    @Operation(summary ="Creates new ECRegistration.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ANCRegistrationDto registration(@Valid @RequestBody ANCRegistrationDto registrationDto);

    @Operation(summary = "Retrieves a ANCRegistration", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{registrationId}")
    public ANCRegistrationDto readANC(@PathVariable("registrationId") String registrationId);

    @Operation(summary = "Updates or Sets a ANCRegistration", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{registrationId}")
    public ANCRegistrationDto patchANC(@PathVariable("registrationId") String registrationId,
                                     @RequestBody ANCRegPatchDto registrationDto);

    @Operation(summary = "Retrieves ANCRegistrations based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    public ANCRegPageResponse filterANCReg(@RequestParam(value = "rchId", required = false) String rchId,
                                          @RequestParam(value = "regId", required = false) String regId,
                                          @RequestParam(value = "dataEntryStatus", required = false) String dataEntryStatus,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size);
}
