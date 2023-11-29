package com.ssf.eligiblecouple.controller;

import com.ssf.eligiblecouple.dtos.EligibleCoupleDto;
import com.ssf.eligiblecouple.dtos.EligibleCouplePageResponse;
import com.ssf.eligiblecouple.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IEligibleCoupleController {

    @Operation(summary = "Creates new ECService", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public EligibleCoupleDto createEc(@Valid @RequestBody EligibleCoupleDto eligibleCoupleDto);

    @Operation(summary = "Retrieves a ECService", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{serviceId}")
    public EligibleCoupleDto readEc(@PathVariable("serviceId") String serviceId);

    @Operation(summary = "Updates or Sets a ECService", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{serviceId}")
    public EligibleCoupleDto patchEc(@PathVariable("serviceId") String serviceId,
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                        schema=@Schema(implementation = EligibleCoupleDto.class)))
                                     @RequestBody Map<String, Object> eligibleCoupleDto);

    @Operation(summary = "Retrieves ECServices based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/filter")
    public EligibleCouplePageResponse filterEc(@RequestParam(value = "rchId", required = false) String rchId,
                                               @RequestParam(value = "serviceId", required = false) String serviceId,
                                               @RequestParam(value = "dataEntryStatus", required = false) String dataEntryStatus,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size);

}
