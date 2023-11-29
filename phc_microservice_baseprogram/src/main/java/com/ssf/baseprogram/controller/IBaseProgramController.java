package com.ssf.baseprogram.controller;

import com.ssf.baseprogram.dtos.BaseProgramDto;
import com.ssf.baseprogram.dtos.BaseProgramPatchDto;
import com.ssf.baseprogram.dtos.PageResponse;
import com.ssf.baseprogram.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

public interface IBaseProgramController {
    @Operation(summary = "Creates new BaseProgram.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public BaseProgramDto create(@Valid @RequestBody BaseProgramDto request);

    @Operation(summary = "Retrieves a PurchaseOrder", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{programId}")
    public BaseProgramDto read(@PathVariable("programId") String id);

    @Operation(summary = "Updates or Sets a BaseProgram's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{programId}")
    public BaseProgramDto patch(@PathVariable("programId") String id,
                                               @RequestBody BaseProgramPatchDto patchDto);

    @Operation(summary = "Retrieves BasePrograms based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    public PageResponse filter(@RequestParam(value= "stDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate stDate,
                                            @RequestParam(value= "edDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate edDate,
                                            @RequestParam(value = "activityName", required = false) String activityName,
                                            @RequestParam(value = "programType", required = false) String programType,
                                            @RequestParam(value = "village", required = false) String village,
                                            @RequestParam(value = "location", required = false) String location,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size);
}
