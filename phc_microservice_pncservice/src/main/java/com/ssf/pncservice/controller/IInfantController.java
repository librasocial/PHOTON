package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.InfantPageResponse;
import com.ssf.pncservice.dtos.PNCPageResponse;
import com.ssf.pncservice.dtos.InfantDto;
import com.ssf.pncservice.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IInfantController {

    @Operation(summary = "Creates new PNCInfants.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/{serviceId}/infants")
    public InfantDto createInfant(@PathVariable("serviceId") String serviceId, @Valid @RequestBody InfantDto infantDto);

    @Operation(summary = "Updates or Sets a PNCInfants", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{serviceId}/infants/{infantId}")
    public InfantDto patchInfant(@PathVariable("serviceId") String serviceId,
                                 @PathVariable("infantId") String infantId,
                                 @RequestBody Map<String, Object> patchDto);

    @Operation(summary = "Retrieves a PNCInfants", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{serviceId}/infants/{infantId}")
    public InfantDto getInfant(@PathVariable("serviceId") String serviceId, @PathVariable("infantId") String infantId);

    @Operation(summary = "Retrieves PNCInfants based on filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/infants/filter")
    public InfantPageResponse getInfantByFilter(@RequestParam(value = "childId", required = false) String childId,
                                                @RequestParam(value = "serviceId", required = false) String serviceId,
                                                @RequestParam(value = "infantId", required = false) String infantId,
                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "size", defaultValue = "5") Integer size);

}
