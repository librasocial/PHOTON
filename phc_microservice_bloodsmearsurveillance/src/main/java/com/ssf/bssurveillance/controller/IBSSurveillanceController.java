package com.ssf.bssurveillance.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssf.bssurveillance.dtos.BSSurveillanceDto;
import com.ssf.bssurveillance.dtos.PageResponse;
import com.ssf.bssurveillance.dtos.PatchDto;
import com.ssf.bssurveillance.dtos.SurveillanceStatusDto;
import com.ssf.bssurveillance.exception.ApiError;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface IBSSurveillanceController {
	@Operation(summary = "Creates new BloodSmearSurveillance.", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@PostMapping
	public BSSurveillanceDto create(@Valid @RequestBody BSSurveillanceDto request);

	@Operation(summary = "Retrieves a Blood Smear Surveillance", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping(value = "/{surveillanceId}")
	public BSSurveillanceDto read(@PathVariable("surveillanceId") String id);

	@Operation(summary = "Patches the Blood Smear Surveillance", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@PatchMapping(value = "/{surveillanceId}")
	public BSSurveillanceDto patch(@PathVariable("surveillanceId") String id, @RequestBody PatchDto patchDto);

	@Operation(summary = "Retrieves BloodSmearSurveillance info", responses = {
			@ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest"),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found") })
	@GetMapping(value = "/filter")
	public PageResponse filter(@RequestParam(value = "citizenId", required = false) String citizenId,
			@RequestParam(value = "slideNo", required = false) String slideNo,
			@RequestParam(value = "type", required = false) String type, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size);

	@GetMapping(value = "/status/filter")
	public List<SurveillanceStatusDto> statusFilter(@RequestParam(value = "citizenIds", required = false) String citizenIds);

}
