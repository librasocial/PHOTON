package com.ssf.bssurveillance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
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
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import com.ssf.bssurveillance.dtos.AuditDto;
import com.ssf.bssurveillance.dtos.BSSurveillanceDto;
import com.ssf.bssurveillance.dtos.PageDto;
import com.ssf.bssurveillance.dtos.PageResponse;
import com.ssf.bssurveillance.dtos.PatchDto;
import com.ssf.bssurveillance.dtos.SurveillanceStatusDto;
import com.ssf.bssurveillance.entities.BSSurveillance;
import com.ssf.bssurveillance.entities.LabResult;
import com.ssf.bssurveillance.entities.Sample;
import com.ssf.bssurveillance.exception.EntityNotFoundException;
import com.ssf.bssurveillance.kafka.KafkaProducer;
import com.ssf.bssurveillance.kafka.message.Actor;
import com.ssf.bssurveillance.kafka.message.Context;
import com.ssf.bssurveillance.kafka.message.KafkaMessage;
import com.ssf.bssurveillance.kafka.topic.Topics;
import com.ssf.bssurveillance.repository.IBSSurveillanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BSSurveillanceService {
	@Autowired
	private IBSSurveillanceRepository repository;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private AuditorAware<String> auditorProvider;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Value("${kafka.enabled}")
	private Boolean kafkaEnabled;

	public BSSurveillanceDto create(BSSurveillanceDto request) {
		log.debug("Creating the bsSurveillance");

		BSSurveillance bsSurveillance = mapper.map(request, BSSurveillance.class);
		repository.save(bsSurveillance);
		BSSurveillanceDto response = mapper.map(bsSurveillance, BSSurveillanceDto.class);

		// Audit save
		AuditDto auditDto = buildAuditDto(bsSurveillance);
		response.setAudit(auditDto);
		if (kafkaEnabled) {
			KafkaMessage<BSSurveillanceDto> kafkaMessage = new KafkaMessage<>();
			kafkaMessage.setContext(Context.builder().language("en")
					.authToken(this.request.getHeader(BSSurveillanceConst.AUTHORIZATION_HEADER)).build());
			kafkaMessage
					.setActor(Actor.builder().id(this.request.getHeader(BSSurveillanceConst.XUSER_ID_HEADER)).build());
			kafkaMessage.setType(Topics.BloodSmearSurveillance + "Created");
			kafkaMessage.setObject(response);
			kafkaProducer.sendMessage(Topics.BloodSmearSurveillance.toString(), kafkaMessage);
		}
		return response;
	}

	public BSSurveillanceDto read(String id) {
		log.debug("Reading the BSSurveillance for id {}", id);
		auditorProvider.getCurrentAuditor();
		BSSurveillance bsSurveillance = getBSSurveillance(id);
		BSSurveillanceDto response = mapper.map(bsSurveillance, BSSurveillanceDto.class);

		// Audit save
		AuditDto auditDto = buildAuditDto(bsSurveillance);
		response.setAudit(auditDto);

		return response;

	}

	public BSSurveillanceDto patch(String id, PatchDto request) {
//        log.debug("Partial Edit for BSSurveillance id  {} ", id);
		var bsSurveillance = getBSSurveillance(id);
		var inputMap = request.getProperties();
		switch (request.getType()) {
		case BS:
			var bsDbMap = objectMapper.convertValue(bsSurveillance, Map.class);
			bsDbMap.putAll(inputMap);
			var surveillance = objectMapper.convertValue(bsDbMap, BSSurveillance.class);
			repository.save(surveillance);
			break;
		case LABRESULT:
			var labDbMap = objectMapper.convertValue(bsSurveillance.getLabResult(), Map.class);
			if(null==labDbMap) {
				labDbMap = new HashMap<>();
			}
			labDbMap.putAll(inputMap);
			var lab = objectMapper.convertValue(labDbMap.get("labResult"), LabResult.class);
			bsSurveillance.setLabResult(lab);
			repository.save(bsSurveillance);
			break;
		case SAMPLE:
			var sampleDbMap = objectMapper.convertValue(bsSurveillance.getSample(), Map.class);
			if(null==sampleDbMap) {
				sampleDbMap = new HashMap<>();
			}
			sampleDbMap.putAll(inputMap);
			var sample = objectMapper.convertValue(sampleDbMap.get("sample"), Sample.class);
			bsSurveillance.setSample(sample);
			repository.save(bsSurveillance);
			break;
		default:
			throw new RuntimeException("Invalid Request Type");
		}
		// response generate
		// Get the updated latest data refreshing here
		bsSurveillance = getBSSurveillance(id);
		BSSurveillanceDto response = mapper.map(bsSurveillance, BSSurveillanceDto.class);
		// Audit save
		AuditDto auditDto = buildAuditDto(bsSurveillance);
		response.setAudit(auditDto);
		if (kafkaEnabled) {
			KafkaMessage<BSSurveillanceDto> kafkaMessage = new KafkaMessage<>();
			kafkaMessage.setContext(Context.builder().language("en")
					.authToken(this.request.getHeader(BSSurveillanceConst.AUTHORIZATION_HEADER)).build());
			kafkaMessage
					.setActor(Actor.builder().id(this.request.getHeader(BSSurveillanceConst.XUSER_ID_HEADER)).build());
			kafkaMessage.setType(Topics.BloodSmearSurveillance + "Updated");
			kafkaMessage.setObject(response);
			kafkaProducer.sendMessage(Topics.BloodSmearSurveillance.toString(), kafkaMessage);
		}
		return response;
	}

	public BSSurveillance getBSSurveillance(String id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("BSSurveillance ::" + id + " not found"));
	}

	public PageResponse filter(String citizenId, String slideNo, String type, int page, int size) {
		List<Criteria> criteria = new ArrayList<>();
		if (citizenId != null)
			criteria.add(Criteria.where("citizenId").is(citizenId));
		if (slideNo != null)
			criteria.add(Criteria.where(("sample.slideNo")).is(slideNo));
		if (type != null)
			criteria.add(Criteria.where(("type")).is(type));
		Criteria criteriaQuery = new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]));
		Query searchQuery = new Query(criteriaQuery);
		Pageable paging = PageRequest.of(page, size);
		if (!criteria.isEmpty())
			return buildPageResponse(repository.query(searchQuery, paging));
		return buildPageResponse(repository.findAll(paging));
	}

	private PageResponse buildPageResponse(Page<BSSurveillance> bsSurveillance) {
		if (bsSurveillance == null || bsSurveillance.isEmpty()) {
			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
			log.debug(" Pagination result is returned " + bsSurveillance.getTotalElements());
			Page<BSSurveillanceDto> dtos = buildResponseDtos(bsSurveillance);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) bsSurveillance.getTotalPages())
							.totalElements(bsSurveillance.getTotalElements()).number(bsSurveillance.getNumber())
							.size(bsSurveillance.getSize()).build())
					.data(dtos.toList()).build();
		}
	}

	private Page<BSSurveillanceDto> buildResponseDtos(Page<BSSurveillance> bsSurveillance) {
		return bsSurveillance.map(p -> {
			// Conversion logic
			BSSurveillanceDto dto = mapper.map(p, BSSurveillanceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(p);
			dto.setAudit(auditDto);
			return dto;
		});
	}

	private AuditDto buildAuditDto(BSSurveillance bsSurveillance) {
		return AuditDto.builder().createdBy(bsSurveillance.getCreatedBy()).modifiedBy(bsSurveillance.getModifiedBy())
				.dateCreated(bsSurveillance.getDateCreated()).dateModified(bsSurveillance.getDateModified()).build();
	}

	public List<SurveillanceStatusDto> statusFilter(List<String> citizenIds) {
		MatchOperation matchStage = Aggregation.match(new Criteria("citizenId").in(citizenIds));
		SortOperation sortByPopDesc = Aggregation.sort(Direction.DESC, "dateModified");
		GroupOperation groupByCitizenId = Aggregation.group("citizenId").first("dateModified").as("dateModified")
				.first("labResult.result").as("result").first("labResult.resultDate").as("resultDate");
		Aggregation aggregation = Aggregation.newAggregation(matchStage, sortByPopDesc, groupByCitizenId);
		AggregationResults<SurveillanceStatusDto> result = mongoTemplate.aggregate(aggregation, BSSurveillance.class,
				SurveillanceStatusDto.class);

		return result.getMappedResults();
	}
}
