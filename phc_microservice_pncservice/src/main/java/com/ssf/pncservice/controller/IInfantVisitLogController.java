package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.InfantVisitLogDto;
import com.ssf.pncservice.dtos.InfantVisitLogPageResponse;
import com.ssf.pncservice.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IInfantVisitLogController {

    @Operation(summary = "Creates new Infant VisitLog.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Badrequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unautorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(value = "/{childId}/infants/visitlogs")
    public InfantVisitLogDto createInfantVisitLog(@PathVariable("childId") String childId, @Valid @RequestBody InfantVisitLogDto request);

    @Operation(summary = "Updates or Sets a InfantsVisitLog", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Badrequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{childId}/infants/visitlogs/{logId}")
    public InfantVisitLogDto patchInfantVisitLog(@PathVariable("childId") String childId, @PathVariable("logId") String logId, @Valid @RequestBody Map<String, Object> request);

    @Operation(summary = "Retrieves a PNCInfant Visitlog", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Badrequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{childId}/infants/visitlogs/{logId}")
    public InfantVisitLogDto readInfantVisitLog(@PathVariable("childId") String childId, @PathVariable("logId") String logId);

    @Operation(summary = "Retrieves PNCInfant VisitLog based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/infants/visitlogs/filter")
    public InfantVisitLogPageResponse filterInfantVisitLog(@RequestParam(value = "childId", required = false) String childId,
                                                           @RequestParam(value = "serviceId", required = false) String serviceId,
                                                           @RequestParam(value = "logId", required = false) String logId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size);

}
