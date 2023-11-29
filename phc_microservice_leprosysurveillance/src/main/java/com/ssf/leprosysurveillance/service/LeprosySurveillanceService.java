package com.ssf.leprosysurveillance.service;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.leprosysurveillance.dto.AuditDto;
import com.ssf.leprosysurveillance.dto.LeprosySurveillanceDto;
import com.ssf.leprosysurveillance.dto.PageDto;
import com.ssf.leprosysurveillance.dto.PageResponse;
import com.ssf.leprosysurveillance.dto.SurveillanceStatusDto;
import com.ssf.leprosysurveillance.exception.InvalidInputException;
import com.ssf.leprosysurveillance.model.LeprosySurveillance;
import com.ssf.leprosysurveillance.repository.LeprosySurveillanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeprosySurveillanceService {

	@Autowired
	LeprosySurveillanceRepository leprosySurveillanceRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	public AuditorAware<String> auditorProvider;

	@Autowired
	private MongoTemplate mongoTemplate;

	public LeprosySurveillanceDto createLeprosySurveillance(LeprosySurveillanceDto request)
			throws InvalidInputException {
		LeprosySurveillance leprosySurveillance = mapper.map(request, LeprosySurveillance.class);
		leprosySurveillance = leprosySurveillanceRepo.insert(leprosySurveillance);
		LeprosySurveillanceDto response = mapper.map(leprosySurveillance, LeprosySurveillanceDto.class);
//		 Audit save
		AuditDto auditDto = buildAuditDto(leprosySurveillance);
		response.setAudit(auditDto);

		return response;
	}

	public LeprosySurveillanceDto updateLeprosySurveillance(String surveillanceId, Map<String, Object> request)
			throws InvalidInputException {
		Optional<LeprosySurveillance> leprosySurveillanceFromDb = leprosySurveillanceRepo.findById(surveillanceId);
		if (leprosySurveillanceFromDb.isPresent()) {
			Map<String, Object> dbMap = objectMapper.convertValue(leprosySurveillanceFromDb.get(), Map.class);
			dbMap.putAll(request);
			LeprosySurveillance data = objectMapper.convertValue(dbMap, LeprosySurveillance.class);

			LeprosySurveillance updatedLeprosySurveillance = leprosySurveillanceRepo.save(data);
			LeprosySurveillanceDto response = mapper.map(updatedLeprosySurveillance, LeprosySurveillanceDto.class);
//			 Audit save
			AuditDto auditDto = buildAuditDto(leprosySurveillanceFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("Surveillance is not available for given ID");
		}
	}

	public LeprosySurveillanceDto readLeprosySurveillance(String surveillanceId) throws InvalidInputException {
		Optional<LeprosySurveillance> leprosySurveillanceFromDb = leprosySurveillanceRepo.findById(surveillanceId);
		if (leprosySurveillanceFromDb.isPresent()) {
			LeprosySurveillanceDto response = mapper.map(leprosySurveillanceFromDb.get(), LeprosySurveillanceDto.class);
			AuditDto auditDto = buildAuditDto(leprosySurveillanceFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("Surveillance is not available for given ID");
		}
	}

	public PageResponse getLeprosySurveillance(String citizenId, String surveillanceId, Integer page, Integer size) {

		auditorProvider.getCurrentAuditor();
		Pageable paging = PageRequest.of(page, size);
		LeprosySurveillance example = LeprosySurveillance.builder().build();

		if (StringUtils.isNotBlank(citizenId)) {
			example.setCitizenId(citizenId);
		}
		if (StringUtils.isNotBlank(surveillanceId)) {
			example.setId(surveillanceId);
		}
		return buildLeprosySurveillancePageResponse(leprosySurveillanceRepo.findAll(Example.of(example), paging));
	}

	private PageResponse buildLeprosySurveillancePageResponse(Page<LeprosySurveillance> leprosySurveillance) {
		if (leprosySurveillance == null || leprosySurveillance.isEmpty()) {
			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
			log.debug(" Pagination result is returned " + leprosySurveillance.getTotalElements());
			Page<LeprosySurveillanceDto> leprosysurveillanceDto = buildLeprosySurveillanceDto(leprosySurveillance);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) leprosySurveillance.getTotalPages())
							.totalElements(leprosySurveillance.getTotalElements())
							.number(leprosySurveillance.getNumber()).size(leprosySurveillance.getSize()).build())
					.data(leprosysurveillanceDto.toList()).build();
		}
	}

	private Page<LeprosySurveillanceDto> buildLeprosySurveillanceDto(Page<LeprosySurveillance> covidSurveillance) {
		return covidSurveillance.map(i -> {
			// Conversion logic
			LeprosySurveillanceDto leprosySurveillanceDto = mapper.map(i, LeprosySurveillanceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(i);
			leprosySurveillanceDto.setAudit(auditDto);
			return leprosySurveillanceDto;
		});
	}

	private AuditDto buildAuditDto(LeprosySurveillance leprosySurveillance) {
		return AuditDto.builder().createdBy(leprosySurveillance.getCreatedBy())
				.modifiedBy(leprosySurveillance.getModifiedBy()).dateCreated(leprosySurveillance.getDateCreated())
				.dateModified(leprosySurveillance.getDateModified()).build();
	}

	public List<SurveillanceStatusDto> statusFilter(List<String> citizenIds) {
		MatchOperation matchStage = Aggregation.match(new Criteria("citizenId").in(citizenIds));
		SortOperation sortByPopDesc = Aggregation.sort(Direction.DESC, "dateModified");
		GroupOperation groupByCitizenId = Aggregation.group("citizenId").first("labResult.result").as("result")
				.first("labResult.resultDate").as("resultDate");
		Aggregation aggregation = Aggregation.newAggregation(matchStage, sortByPopDesc, groupByCitizenId);
		AggregationResults<SurveillanceStatusDto> result = mongoTemplate.aggregate(aggregation,
				LeprosySurveillance.class, SurveillanceStatusDto.class);

		return result.getMappedResults();
	}
}
