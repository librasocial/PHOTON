package com.ssf.leprosysurveillance.controller;

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

import com.ssf.leprosysurveillance.dto.LeprosySurveillanceDto;
import com.ssf.leprosysurveillance.dto.PageResponse;
import com.ssf.leprosysurveillance.dto.SurveillanceStatusDto;
import com.ssf.leprosysurveillance.exception.InvalidInputException;
import com.ssf.leprosysurveillance.service.LeprosySurveillanceService;

@RestController
@RequestMapping("/leprosySurveillances")
public class LeprosySurveillanceController {

	@Autowired
	LeprosySurveillanceService leprosySurveillanceService;

	@PostMapping
	public ResponseEntity<LeprosySurveillanceDto> createLeprosySurveillance(@RequestBody LeprosySurveillanceDto request)
			throws InvalidInputException {
		return new ResponseEntity<LeprosySurveillanceDto>(leprosySurveillanceService.createLeprosySurveillance(request),
				HttpStatus.CREATED);
	}

	@PatchMapping("/{surveillanceId}")
	public ResponseEntity<LeprosySurveillanceDto> updateLeprosySurveillance(
			@PathVariable("surveillanceId") String surveillanceId, @RequestBody Map<String, Object> request)
			throws InvalidInputException {
		return new ResponseEntity<LeprosySurveillanceDto>(
				leprosySurveillanceService.updateLeprosySurveillance(surveillanceId, request), HttpStatus.OK);
	}

	@GetMapping("/{surveillanceId}")
	public ResponseEntity<LeprosySurveillanceDto> readLeprosySurveillance(
			@PathVariable("surveillanceId") String surveillanceId) throws InvalidInputException {
		return new ResponseEntity<LeprosySurveillanceDto>(
				leprosySurveillanceService.readLeprosySurveillance(surveillanceId), HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<PageResponse> getLeprosySurveillanceByFilter(
			@RequestParam(value = "citizenId", required = false) String citizenId,
			@RequestParam(value = "surveillanceId", required = false) String surveillanceId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws InvalidInputException {
		return new ResponseEntity<PageResponse>(
				leprosySurveillanceService.getLeprosySurveillance(citizenId,surveillanceId, page, size), HttpStatus.OK);
	}
	
	@GetMapping(value = "/status/filter")
	public List<SurveillanceStatusDto> statusFilter(@RequestParam(value = "citizenIds", required = false) String citizenIds) {
		return leprosySurveillanceService.statusFilter(List.of(citizenIds.split(",")));

	}
}
