package com.ssf.eligiblecouple.controller;

import com.ssf.eligiblecouple.dtos.VisitLogDto;
import com.ssf.eligiblecouple.dtos.VisitLogDtoPageResponse;
import com.ssf.eligiblecouple.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IVisitLogController {

    @Operation(summary = "Creates new VisitLog", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(value = "/{serviceId}/visitlogs")
    public VisitLogDto createVisitLog(@PathVariable("serviceId") String serviceId, @Valid @RequestBody VisitLogDto request);

    @Operation(summary = "Updates or Sets a VisitLog", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{serviceId}/visitlogs/{logId}")
    public VisitLogDto patchVisitLog(@PathVariable("serviceId") String serviceId, @PathVariable("logId") String logId, @Valid @RequestBody Map<String, Object> request);

    @Operation(summary = "Retrieves a ECService VisitLog", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{serviceId}/visitlogs/{logId}")
    public VisitLogDto readVisitLog(@PathVariable("serviceId") String serviceId, @PathVariable("logId") String logId);

    @Operation(summary = "Retrieves ECServices based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Un-autorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/visitlogs/filter")
    public VisitLogDtoPageResponse filterVisitLog(@RequestParam(value = "serviceId", required = false) String serviceId,
                                                  @RequestParam(value = "logId", required = false) String logId,
                                                  @RequestParam(value = "rchId", required = false) String rchId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size);

}
