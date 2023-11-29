package com.ssf.immunizationrec.controller;

import com.ssf.immunizationrec.dtos.FilterDto;
import com.ssf.immunizationrec.dtos.ImmunizationRecDto;
import com.ssf.immunizationrec.dtos.PageResponse;
import com.ssf.immunizationrec.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IImmunizationRecController {

    @Operation(summary = "Creates new immunizationRecommendation.", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ImmunizationRecDto createImmunRec(@Valid @RequestBody ImmunizationRecDto request);

    @Operation(summary = "Retrieves a immunizationRecommendation", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(value = "/{immunizationRecommendationId}")
    public ImmunizationRecDto readImmunRec(@PathVariable("immunizationRecommendationId") String id);

    @Operation(summary = "Filter immunizationRecommendations based on filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(value = "/filter")
    public PageResponse getImmunRecByFilter(@RequestBody FilterDto filterDto);

    @Operation(summary = "Updates or Sets a immunizationRecommendation's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping(value = "/{immunizationRecommendationId}")
    public ImmunizationRecDto patchImmunRec(@PathVariable("immunizationRecommendationId") String id, @RequestBody  Map<String, Object> request);


}
