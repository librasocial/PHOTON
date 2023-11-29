package com.ssf.childcareimmunization.controller;

import java.util.List;
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

import com.ssf.childcareimmunization.dtos.ImmunizationDto;
import com.ssf.childcareimmunization.dtos.ImmunizationGroupByDto;
import com.ssf.childcareimmunization.dtos.PageResponse;
import com.ssf.childcareimmunization.exception.InvalidInputException;
import com.ssf.childcareimmunization.service.ImmunizationService;

@RestController
@RequestMapping("/immunizations")
public class ImmunizationController {
	@Autowired
	ImmunizationService immunizationService;

	@PostMapping
	public ResponseEntity<ImmunizationDto> createImmunization(@RequestBody ImmunizationDto request)
			throws InvalidInputException {
		System.out.println("enter");
		return new ResponseEntity<ImmunizationDto>(immunizationService.createImmunization(request), HttpStatus.CREATED);
	}

	@GetMapping("/{immunizationid}")
	public ResponseEntity<ImmunizationDto> getImmunization(@PathVariable("immunizationid") String immunizationId)
			throws InvalidInputException {
		return new ResponseEntity<ImmunizationDto>(immunizationService.getImmunization(immunizationId), HttpStatus.OK);
	}

	@PatchMapping("/{immunizationid}")
	public ResponseEntity<ImmunizationDto> updateImmunization(@PathVariable("immunizationid") String immunizationId,
			@RequestBody Map<String, Object> request) throws InvalidInputException {
		return new ResponseEntity<ImmunizationDto>(immunizationService.updateImmunization(immunizationId, request),
				HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<PageResponse> getImmunizationByFilter(
			@RequestParam(value = "immunizationId", required = false) String immunizationId,
			@RequestParam(value = "childCitizenId", required = false) String childCitizenId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws InvalidInputException {
		return new ResponseEntity<PageResponse>(
				immunizationService.getImmunizationByFilter(immunizationId, childCitizenId, page, size), HttpStatus.OK);
	}

	@GetMapping("/groupby/{ccitizenId}")
	public ResponseEntity<Map<String, List<ImmunizationGroupByDto>>> getGroupeByImmunization(
			@PathVariable("ccitizenId") String ccitizenId) throws InvalidInputException {
		return new ResponseEntity<Map<String, List<ImmunizationGroupByDto>>>(
				immunizationService.getGroupedDose(ccitizenId), HttpStatus.OK);
	}

	@PostMapping("/createbulkimmunization")
	public ResponseEntity<String> createImmunizationList(@RequestBody List<ImmunizationDto> listOfImmunization)
			throws InvalidInputException {
		return new ResponseEntity<String>(immunizationService.createImmunizationList(listOfImmunization),
				HttpStatus.CREATED);
	}

	@GetMapping("/filterbyccid")
	public ResponseEntity<ImmunizationDto> getImmunizationByChildId(
			@RequestParam(value = "childCitizenId", required = true) String childCitizenId)
			throws InvalidInputException {
		return new ResponseEntity<ImmunizationDto>(immunizationService.getImmunizationByChildId(childCitizenId),
				HttpStatus.OK);
	}

}
