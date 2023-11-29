package com.ssf.tbsurveillance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.ssf.tbsurveillance.constants.Constants;
import com.ssf.tbsurveillance.dtos.AuditDto;
import com.ssf.tbsurveillance.dtos.PageDto;
import com.ssf.tbsurveillance.dtos.PageResponse;
import com.ssf.tbsurveillance.dtos.SurveillanceStatusDto;
import com.ssf.tbsurveillance.dtos.TbSurveillanceDto;
import com.ssf.tbsurveillance.exception.InvalidInputException;
import com.ssf.tbsurveillance.kafka.KafkaProducer;
import com.ssf.tbsurveillance.kafka.message.Actor;
import com.ssf.tbsurveillance.kafka.message.Context;
import com.ssf.tbsurveillance.kafka.message.KafkaMessage;
import com.ssf.tbsurveillance.kafka.topic.Topics;
import com.ssf.tbsurveillance.model.TbSurveillance;
import com.ssf.tbsurveillance.repository.TbSurveillanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TbSurveillanceService {

	@Autowired
	TbSurveillanceRepository tbRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	public AuditorAware<String> auditorProvider;

	@Value("${kafka.enabled}")
	private Boolean kafkaEnabled;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public TbSurveillanceDto createTbSurveillance(TbSurveillanceDto request) throws InvalidInputException {
		TbSurveillance tbSurveillance = mapper.map(request, TbSurveillance.class);
		tbSurveillance = tbRepo.save(tbSurveillance);
		TbSurveillanceDto response = mapper.map(tbSurveillance, TbSurveillanceDto.class);
//		 Audit save
		AuditDto auditDto = buildAuditDto(tbSurveillance);
		response.setAudit(auditDto);

		if (kafkaEnabled) {
			KafkaMessage<TbSurveillanceDto> kafkaMessage = new KafkaMessage<>();
			kafkaMessage.setContext(Context.builder().language("en")
					.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
			kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
			kafkaMessage.setType(Topics.TBSurveillance + "Created");
			kafkaMessage.setObject(response);
			kafkaProducer.sendMessage(Topics.TBSurveillance.toString(), kafkaMessage);
		}
		return response;
	}

	public TbSurveillanceDto updateTbSurveillance(String surveillanceId, Map<String, Object> request)
			throws InvalidInputException {
		Optional<TbSurveillance> tbSurveillanceFromDb = tbRepo.findById(surveillanceId);
		if (tbSurveillanceFromDb.isPresent()) {
			Map<String, Object> dbMap = objectMapper.convertValue(tbSurveillanceFromDb.get(), Map.class);
			dbMap.putAll(request);
			TbSurveillance data = objectMapper.convertValue(dbMap, TbSurveillance.class);
			TbSurveillance updatedTbSurveillance = tbRepo.save(data);
			TbSurveillanceDto response = mapper.map(updatedTbSurveillance, TbSurveillanceDto.class);
			if (kafkaEnabled) {
				KafkaMessage<TbSurveillanceDto> kafkaMessage = new KafkaMessage<>();
				kafkaMessage.setContext(Context.builder().language("en")
						.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
				kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
				kafkaMessage.setType(Topics.TBSurveillance + "Updated");
				kafkaMessage.setObject(response);
				kafkaProducer.sendMessage(Topics.TBSurveillance.toString(), kafkaMessage);
			}
			return response;
		} else {
			throw new InvalidInputException("Tb surveillance is not available for given ID");
		}
	}

	public TbSurveillanceDto getTbSurveillance(String surveillanceId) throws InvalidInputException {
		Optional<TbSurveillance> tbSurveillanceFromDb = tbRepo.findById(surveillanceId);
		if (tbSurveillanceFromDb.isPresent()) {
			TbSurveillanceDto response = mapper.map(tbSurveillanceFromDb.get(), TbSurveillanceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(tbSurveillanceFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("Tb surveillance is not available for given ID");
		}
	}

	private AuditDto getCreatedByAuditor() {
		String username = "";
		Optional<String> optional = auditorProvider.getCurrentAuditor();
		if (optional.isPresent()) {
			username = optional.get();
		}
		return AuditDto.builder().modifiedBy(username).createdBy(username).build();
	}

	private AuditDto getModifiedByAuditor(AuditDto auditDTO) {
		String username = "";
		Optional<String> optional = auditorProvider.getCurrentAuditor();
		if (optional.isPresent()) {
			username = optional.get();
		}
		auditDTO.setModifiedBy(username);
		return auditDTO;
	}

	public PageResponse getTbSurveillanceByFilter(String citizenId, String surveillanceId, Integer page, Integer size) {
//		log.debug("filter the TBSurveillance by citizenId {},  surveillanceId  {} ", citizenId, surveillanceId);

		auditorProvider.getCurrentAuditor();
		Pageable paging = PageRequest.of(page, size);
		TbSurveillance example = TbSurveillance.builder().build();

		if (StringUtils.isNotBlank(citizenId)) {
			example.setCitizenId(citizenId);
		}
		if (StringUtils.isNotBlank(surveillanceId)) {
			example.setId(surveillanceId);
		}

		return buildTbSurveillancePageResponse(tbRepo.findAll(Example.of(example), paging));
	}

	private PageResponse buildTbSurveillancePageResponse(Page<TbSurveillance> tbSurveillance) {
		if (tbSurveillance == null || tbSurveillance.isEmpty()) {
//			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
//			log.debug(" Pagination result is returned " + tbSurveillance.getTotalElements());
			Page<TbSurveillanceDto> tbSurveillanceDto = buildTbSurveillanceDto(tbSurveillance);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) tbSurveillance.getTotalPages())
							.totalElements(tbSurveillance.getTotalElements()).number(tbSurveillance.getNumber())
							.size(tbSurveillance.getSize()).build())
					.data(tbSurveillanceDto.toList()).build();
		}
	}

	private Page<TbSurveillanceDto> buildTbSurveillanceDto(Page<TbSurveillance> tbSurveillance) {
		return tbSurveillance.map(i -> {
			// Conversion logic
			TbSurveillanceDto covidSurveillanceDto = mapper.map(i, TbSurveillanceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(i);
			covidSurveillanceDto.setAudit(auditDto);
			return covidSurveillanceDto;
		});
	}

	private AuditDto buildAuditDto(TbSurveillance tbSurveillance) {
		return AuditDto.builder().createdBy(tbSurveillance.getCreatedBy()).modifiedBy(tbSurveillance.getModifiedBy())
				.dateCreated(tbSurveillance.getDateCreated()).dateModified(tbSurveillance.getDateModified()).build();
	}
	
	public List<SurveillanceStatusDto> statusFilter(List<String> citizenIds) {
		MatchOperation matchStage = Aggregation.match(new Criteria("citizenId").in(citizenIds));
		SortOperation sortByPopDesc = Aggregation.sort(Direction.DESC, "dateModified");
		GroupOperation groupByCitizenId = Aggregation.group("citizenId").first("dateModified").as("dateModified")
				.first("labResult.result").as("result").first("labResult.resultDate").as("resultDate");
		Aggregation aggregation = Aggregation.newAggregation(matchStage, sortByPopDesc, groupByCitizenId);
		AggregationResults<SurveillanceStatusDto> result = mongoTemplate.aggregate(aggregation, TbSurveillance.class,
				SurveillanceStatusDto.class);

		return result.getMappedResults();
	}

}
