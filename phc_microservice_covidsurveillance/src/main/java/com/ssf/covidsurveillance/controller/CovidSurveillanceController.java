package com.ssf.covidsurveillance.controller;

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

import com.ssf.covidsurveillance.dtos.CovidSurveillanceDto;
import com.ssf.covidsurveillance.dtos.PageResponse;
import com.ssf.covidsurveillance.exception.InvalidInputException;
import com.ssf.covidsurveillance.service.CovidSurveillanceService;

@RestController
@RequestMapping("/covidsurveillances")
public class CovidSurveillanceController {

	@Autowired
	CovidSurveillanceService covidService;

	@PostMapping
	public ResponseEntity<CovidSurveillanceDto> createCovidSurveillance(
			@RequestBody CovidSurveillanceDto covidSurveillanceDto) throws InvalidInputException {
		return new ResponseEntity<CovidSurveillanceDto>(covidService.createCovidSurveillance(covidSurveillanceDto),
				HttpStatus.CREATED);
	}

	@GetMapping("/{surveillanceId}")
	public ResponseEntity<CovidSurveillanceDto> readCovidSurveillance(
			@PathVariable("surveillanceId") String surveillanceId) throws InvalidInputException {
		return new ResponseEntity<CovidSurveillanceDto>(covidService.readCovidSurveillance(surveillanceId),
				HttpStatus.OK);

	}

	@PatchMapping("/{surveillanceId}")
	public ResponseEntity<CovidSurveillanceDto> updateCovidSurveillance(
			@PathVariable("surveillanceId") String surveillanceId, @RequestBody Map<String, Object> request)
			throws InvalidInputException {
		return new ResponseEntity<CovidSurveillanceDto>(covidService.updateCovidSurveillance(surveillanceId, request),
				HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<PageResponse> getCovidSurveillanceByFilter(
			@RequestParam(value = "citizenId", required = false) String citizenId,
			@RequestParam(value = "surveillanceId", required = false) String surveillanceId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws InvalidInputException {
		return new ResponseEntity<PageResponse>(
				covidService.getCovidSurveillanceByFilter(citizenId, surveillanceId, page, size), HttpStatus.OK);
	}

}
