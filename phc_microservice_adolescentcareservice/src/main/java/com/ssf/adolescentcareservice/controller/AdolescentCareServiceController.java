package com.ssf.adolescentcareservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssf.adolescentcareservice.dtos.AdolescentCareServiceDto;
import com.ssf.adolescentcareservice.dtos.PageResponse;
import com.ssf.adolescentcareservice.exception.InvalidInputException;
import com.ssf.adolescentcareservice.service.AdolescentCareServiceService;

@RestController
@RequestMapping("/adolescentcareservice")
public class AdolescentCareServiceController {
	@Autowired
	AdolescentCareServiceService adolescentCareService;

	@PostMapping
	public ResponseEntity<AdolescentCareServiceDto> createAdolescentCare(@RequestBody AdolescentCareServiceDto request)
			throws InvalidInputException {
		return new ResponseEntity<AdolescentCareServiceDto>(adolescentCareService.createAdolescentCareService(request),
				HttpStatus.CREATED);
	}

	@GetMapping("/{adolescentcareserviceid}")
	public ResponseEntity<AdolescentCareServiceDto> getAdolescentCare(
			@PathVariable("adolescentcareserviceid") String adolescentCareServiceId) throws InvalidInputException {
		return new ResponseEntity<AdolescentCareServiceDto>(
				adolescentCareService.getAdolescentCareService(adolescentCareServiceId), HttpStatus.OK);
	}

	@PatchMapping("/{adolescentcareserviceid}")
	public ResponseEntity<AdolescentCareServiceDto> updateAdolescentCareService(
			@PathVariable("adolescentcareserviceid") String adolescentCareServiceId,
			@RequestBody Map<String, Object> request) throws InvalidInputException {
		return new ResponseEntity<AdolescentCareServiceDto>(
				adolescentCareService.updateAdolescentCareService(adolescentCareServiceId, request), HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<PageResponse> getAdolescentCareServiceByFilter(
			@RequestParam(value = "childCitizenId", required = false) String childCitizenId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws InvalidInputException {
		return new ResponseEntity<PageResponse>(
				adolescentCareService.getAdolescentCareServiceByFilter(childCitizenId, page, size), HttpStatus.OK);
	}

}
