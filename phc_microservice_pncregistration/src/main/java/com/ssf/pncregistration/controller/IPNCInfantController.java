package com.ssf.pncregistration.controller;

import com.ssf.pncregistration.dtos.*;
import com.ssf.pncregistration.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface IPNCInfantController {

    @Operation(summary ="Creates new Infant.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{registrationId}/infants")
    public List<InfantDto> createInfant(@PathVariable("registrationId") String registrationId, @Valid @RequestBody Map<String,List<InfantDto>> infantDto);

    @Operation(summary = "Retrieves an Infant", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{registrationId}/infants/{id}")
    public InfantDto readInfants(@PathVariable("registrationId") String registrationId, @PathVariable("id") String infantId);

    @Operation(summary = "Updates or Sets an Infant", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{registrationId}/infants/{id}")
    public InfantDto patchInfant(@PathVariable("registrationId") String registrationId,@PathVariable("id") String infantId,
                                     @RequestBody InfantPatchDto infantPatchDto);

    @Operation(summary = "Retrieves Infants based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/infants/filter")
    public InfantPageResponse filterInfants(@RequestParam(value = "infantId", required = false) String infantId,
                                           @RequestParam(value = "registrationId", required = false) String regId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size);
}
