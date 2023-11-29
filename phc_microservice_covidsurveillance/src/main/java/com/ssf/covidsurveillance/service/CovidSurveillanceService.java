package com.ssf.covidsurveillance.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.covidsurveillance.dtos.AuditDto;
import com.ssf.covidsurveillance.dtos.CovidSurveillanceDto;
import com.ssf.covidsurveillance.dtos.PageDto;
import com.ssf.covidsurveillance.dtos.PageResponse;
import com.ssf.covidsurveillance.entities.CovidSurveillance;
import com.ssf.covidsurveillance.exception.InvalidInputException;
import com.ssf.covidsurveillance.repository.CovidSurveillanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CovidSurveillanceService {
	@Autowired
	CovidSurveillanceRepository covidRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	public AuditorAware<String> auditorProvider;

	@Autowired
	private ObjectMapper objectMapper;

	public CovidSurveillanceDto createCovidSurveillance(CovidSurveillanceDto request) throws InvalidInputException {
		CovidSurveillance covidSurveillance = mapper.map(request, CovidSurveillance.class);
		covidSurveillance = covidRepo.insert(covidSurveillance);
		CovidSurveillanceDto response = mapper.map(covidSurveillance, CovidSurveillanceDto.class);
//		 Audit save
		AuditDto auditDto = buildAuditDto(covidSurveillance);
		response.setAudit(auditDto);
		return response;
	}

	public CovidSurveillanceDto readCovidSurveillance(String surveillanceId) throws InvalidInputException {
		Optional<CovidSurveillance> covidSurveillanceFromDb = covidRepo.findById(surveillanceId);
		if (covidSurveillanceFromDb.isPresent()) {
			CovidSurveillanceDto response = mapper.map(covidSurveillanceFromDb.get(), CovidSurveillanceDto.class);
//			 Audit save
			AuditDto auditDto = buildAuditDto(covidSurveillanceFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("Surveillance is not available for given ID");
		}
	}

	public CovidSurveillanceDto updateCovidSurveillance(String surveillanceId, Map<String, Object> request)
			throws InvalidInputException {
		Optional<CovidSurveillance> covidSurveillanceFromDb = covidRepo.findById(surveillanceId);
		if (covidSurveillanceFromDb.isPresent()) {
			Map<String, Object> dbMap = objectMapper.convertValue(covidSurveillanceFromDb.get(), Map.class);
			dbMap.putAll(request);
			CovidSurveillance data = objectMapper.convertValue(dbMap, CovidSurveillance.class);

			CovidSurveillance updatedCovidSurveillance = covidRepo.save(data);
			CovidSurveillanceDto response = mapper.map(updatedCovidSurveillance, CovidSurveillanceDto.class);
			AuditDto auditDto = buildAuditDto(covidSurveillanceFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("CovidSurveillanceService is not available for given ID");
		}
	}

	public PageResponse getCovidSurveillanceByFilter(String citizenId, String surveillanceId, Integer page,
			Integer size) {
		log.debug("filter the CovidSurveillance by citizenId {},  surveillanceId  {} ", citizenId, surveillanceId);

		auditorProvider.getCurrentAuditor();
		Pageable paging = PageRequest.of(page, size);
		CovidSurveillance example = CovidSurveillance.builder().build();

		if (StringUtils.isNotBlank(citizenId)) {
			example.setCitizenId(citizenId);
		}
		if (StringUtils.isNotBlank(surveillanceId)) {
			example.setId(surveillanceId);
		}

		return buildCovidSurveillancePageResponse(covidRepo.findAll(Example.of(example), paging));
	}

	private PageResponse buildCovidSurveillancePageResponse(Page<CovidSurveillance> covidSurveillance) {
		if (covidSurveillance == null || covidSurveillance.isEmpty()) {
			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
			log.debug(" Pagination result is returned " + covidSurveillance.getTotalElements());
			Page<CovidSurveillanceDto> infantDtoPage = buildPCovidSurveillanceDto(covidSurveillance);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) covidSurveillance.getTotalPages())
							.totalElements(covidSurveillance.getTotalElements()).number(covidSurveillance.getNumber())
							.size(covidSurveillance.getSize()).build())
					.data(infantDtoPage.toList()).build();
		}
	}

	private Page<CovidSurveillanceDto> buildPCovidSurveillanceDto(Page<CovidSurveillance> covidSurveillance) {
		return covidSurveillance.map(i -> {
			// Conversion logic
			CovidSurveillanceDto covidSurveillanceDto = mapper.map(i, CovidSurveillanceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(i);
			covidSurveillanceDto.setAudit(auditDto);
			return covidSurveillanceDto;
		});
	}

	private AuditDto buildAuditDto(CovidSurveillance covidSurveillance) {
		return AuditDto.builder().createdBy(covidSurveillance.getCreatedBy())
				.modifiedBy(covidSurveillance.getModifiedBy())
				.dateCreated(covidSurveillance.getDateCreated())
				.dateModified(covidSurveillance.getDateModified()).build();
	}
}
