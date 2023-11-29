package com.ssf.childcareservice.controller;

import com.ssf.childcareservice.dtos.ChildCareDto;
import com.ssf.childcareservice.dtos.PageResponse;
import com.ssf.childcareservice.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

public interface IChildCareController {
	@Operation(summary = "Creates new ChildCare registration.", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@PostMapping
	public ChildCareDto createChildCare(@Valid @RequestBody ChildCareDto request);

	@Operation(summary = "Retrieves a vital sign info.", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping(value = "/{childCareId}")
	public ChildCareDto readChildCare(@PathVariable("childCareId") String childCareId);

	@Operation(summary = "Retrieves ChildCare info", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@GetMapping(value = "/filter")
	public PageResponse getChildCareByFilter(@RequestParam(value = "citizenId", required = false) String citizenId,
			@RequestParam(value = "childId", required = false) String childId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size);

	@Operation(summary = "Patches the Vital Sign", responses = {
			@ApiResponse(responseCode = "201", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "The specified resource was not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@PatchMapping(value = "/{childCareId}")
	public ChildCareDto patchChildCare(@PathVariable("childCareId") String childCareId,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = ChildCareDto.class))) @RequestBody Map<String, Object> patchDto);

}
