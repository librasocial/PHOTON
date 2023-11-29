package com.ssf.tbsurveillance.controller;

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

import com.ssf.tbsurveillance.dtos.PageResponse;
import com.ssf.tbsurveillance.dtos.SurveillanceStatusDto;
import com.ssf.tbsurveillance.dtos.TbSurveillanceDto;
import com.ssf.tbsurveillance.exception.InvalidInputException;
import com.ssf.tbsurveillance.service.TbSurveillanceService;

@RestController
@RequestMapping("/tbsurveillance")
public class TbSurveillanceController {

	@Autowired
	TbSurveillanceService TbSurveillanceService;

	@PostMapping
	public ResponseEntity<TbSurveillanceDto> createTbSurveillance(@RequestBody TbSurveillanceDto tbSurveillance)
			throws InvalidInputException {
		return new ResponseEntity<TbSurveillanceDto>(TbSurveillanceService.createTbSurveillance(tbSurveillance),
				HttpStatus.CREATED);
	}

	@PatchMapping("/{surveillanceId}")
	public ResponseEntity<TbSurveillanceDto> updateTbSurveillance(@PathVariable("surveillanceId") String surveillanceId,
			@RequestBody Map<String, Object> request) throws InvalidInputException {
		return new ResponseEntity<TbSurveillanceDto>(
				TbSurveillanceService.updateTbSurveillance(surveillanceId, request), HttpStatus.OK);
	}

	@GetMapping("/{surveillanceId}")
	public ResponseEntity<TbSurveillanceDto> getTbSurveillance(@PathVariable("surveillanceId") String surveillanceId)
			throws InvalidInputException {
		return new ResponseEntity<TbSurveillanceDto>(TbSurveillanceService.getTbSurveillance(surveillanceId),
				HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<PageResponse> getTbSurveillanceByFilter(
			@RequestParam(value = "citizenId", required = false) String citizenId,
			@RequestParam(value = "surveillanceId", required = false) String surveillanceId,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) throws InvalidInputException {
		return new ResponseEntity<PageResponse>(
				TbSurveillanceService.getTbSurveillanceByFilter(citizenId, surveillanceId, page, size), HttpStatus.OK);
	}

	@GetMapping(value = "/status/filter")
	public List<SurveillanceStatusDto> statusFilter(@RequestParam(value = "citizenIds", required = false) String citizenIds) {
		return TbSurveillanceService.statusFilter(List.of(citizenIds.split(",")));
	}
}
