package com.ssf.adolescentcare.controller;

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

import com.ssf.adolescentcare.dtos.AdolescentCareDto;
import com.ssf.adolescentcare.dtos.PageResponse;
import com.ssf.adolescentcare.exception.InvalidInputException;
import com.ssf.adolescentcare.service.AdolescentCareService;

@RestController
@RequestMapping("/adolescentcare")
public class AdolescentCareController {
	@Autowired
	AdolescentCareService adolescentcare;

	@PostMapping
	public ResponseEntity<AdolescentCareDto> createAdolescentCare(@RequestBody AdolescentCareDto request)
			throws InvalidInputException {
		return new ResponseEntity<AdolescentCareDto>(adolescentcare.createAdolescentCare(request), HttpStatus.CREATED);
	}

	@GetMapping("/{adolescentCareId}")
	public ResponseEntity<AdolescentCareDto> getAdolescentCare(
			@PathVariable("adolescentCareId") String adolescentCareId) throws InvalidInputException {
		return new ResponseEntity<AdolescentCareDto>(adolescentcare.getAdolescentCare(adolescentCareId), HttpStatus.OK);
	}

	@PatchMapping("/{adolescentCareid}")
	public ResponseEntity<AdolescentCareDto> updateAdolescentCare(
			@PathVariable("adolescentCareid") String adolescentCareId, @RequestBody Map<String, Object> request)
			throws InvalidInputException {
		return new ResponseEntity<AdolescentCareDto>(adolescentcare.updateAdolescentCare(adolescentCareId, request),
				HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<PageResponse> getAdolescentCareByFilter(
			@RequestParam(value = "childCitizenId", required = false) String childCitizenId,
			@RequestParam(value = "rchId", required = false) String rchId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws InvalidInputException {
		return new ResponseEntity<PageResponse>(
				adolescentcare.getAdolescentCareByFilter(childCitizenId, rchId, page, size), HttpStatus.OK);
	}

}
