package com.ssf.adolescentcare.service;

import java.util.ArrayList;
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
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.adolescentcare.constants.Constants;
import com.ssf.adolescentcare.dtos.AdolescentCareDto;
import com.ssf.adolescentcare.dtos.AuditDto;
import com.ssf.adolescentcare.dtos.PageDto;
import com.ssf.adolescentcare.dtos.PageResponse;
import com.ssf.adolescentcare.entities.AdolescentCare;
import com.ssf.adolescentcare.exception.InvalidInputException;
import com.ssf.adolescentcare.kafka.KafkaProducer;
import com.ssf.adolescentcare.kafka.message.Actor;
import com.ssf.adolescentcare.kafka.message.Context;
import com.ssf.adolescentcare.kafka.message.KafkaMessage;
import com.ssf.adolescentcare.kafka.topic.Topics;
import com.ssf.adolescentcare.repository.AdolescentCareRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdolescentCareService {
	@Autowired
	AdolescentCareRepository adolescentRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Value("${kafka.enabled}")
	private boolean kafkaEnabled;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	public AuditorAware<String> auditorProvider;

	public AdolescentCareDto createAdolescentCare(AdolescentCareDto request) {
		AdolescentCare adolescentCare = mapper.map(request, AdolescentCare.class);
		adolescentCare = adolescentRepo.insert(adolescentCare);
		AdolescentCareDto response = mapper.map(adolescentCare, AdolescentCareDto.class);
		// Audit save
		AuditDto auditDto = buildAuditDto(adolescentCare);
		response.setAudit(auditDto);
		if (kafkaEnabled) {
			KafkaMessage<AdolescentCareDto> kafkaMessage = new KafkaMessage<>();
			kafkaMessage.setContext(Context.builder().language("en")
					.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
			kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
			kafkaMessage.setType(Topics.AdolescentCare + "Created");
			kafkaMessage.setObject(response);
			kafkaProducer.sendMessage(Topics.AdolescentCare.toString(), kafkaMessage);
		}

		return response;
	}

	public AdolescentCareDto getAdolescentCare(String adolescentCareId) throws InvalidInputException {
		Optional<AdolescentCare> adolescentCareFromDb = adolescentRepo.findById(adolescentCareId);
		if (adolescentCareFromDb.isPresent()) {
			AdolescentCareDto response = mapper.map(adolescentCareFromDb.get(), AdolescentCareDto.class);
			AuditDto auditDto = buildAuditDto(adolescentCareFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("AdolescentCare is not available for given ID");
		}
	}

	public AdolescentCareDto updateAdolescentCare(String adolescentCareId, Map<String, Object> request)
			throws InvalidInputException {
		Optional<AdolescentCare> adolescentCareFromDb = adolescentRepo.findById(adolescentCareId);
		if (adolescentCareFromDb.isPresent()) {

			Map<String, Object> dbMap = objectMapper.convertValue(adolescentCareFromDb.get(), Map.class);
			dbMap.putAll(request);
			AdolescentCare data = objectMapper.convertValue(dbMap, AdolescentCare.class);
			AdolescentCare updatedAdolescentCare = adolescentRepo.save(data);
			AdolescentCareDto response = mapper.map(updatedAdolescentCare, AdolescentCareDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(adolescentCareFromDb.get());
			response.setAudit(auditDto);
			if (kafkaEnabled) {
				KafkaMessage<AdolescentCareDto> kafkaMessage = new KafkaMessage<>();
				kafkaMessage.setContext(Context.builder().language("en")
						.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
				kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
				kafkaMessage.setType(Topics.AdolescentCare + "Updated");
				kafkaMessage.setObject(response);
				kafkaProducer.sendMessage(Topics.AdolescentCare.toString(), kafkaMessage);
			}
			return response;
		} else {
			throw new InvalidInputException("AdolescentCare is not available for given ID");
		}
	}

	public PageResponse getAdolescentCareByFilter(String citizenId, String rchId, Integer page, Integer size) {
		log.debug("filter the AdolescentCare by citizenId {},  rchId  {} ", citizenId, rchId);

		auditorProvider.getCurrentAuditor();
		Pageable paging = PageRequest.of(page, size);
		AdolescentCare example = AdolescentCare.builder().build();

		if (StringUtils.isNotBlank(citizenId)) {
			example.setCitizenId(citizenId);
		}
		if (StringUtils.isNotBlank(rchId)) {
			example.setId(rchId);
		}

		return buildAdolescentCarePageResponse(adolescentRepo.findAll(Example.of(example), paging));
	}

	private PageResponse buildAdolescentCarePageResponse(Page<AdolescentCare> adolescentCare) {
		if (adolescentCare == null || adolescentCare.isEmpty()) {
			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
			log.debug(" Pagination result is returned " + adolescentCare.getTotalElements());
			Page<AdolescentCareDto> infantDtoPage = buildAdolescentCareDto(adolescentCare);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) adolescentCare.getTotalPages())
							.totalElements(adolescentCare.getTotalElements()).number(adolescentCare.getNumber())
							.size(adolescentCare.getSize()).build())
					.data(infantDtoPage.toList()).build();
		}
	}

	private Page<AdolescentCareDto> buildAdolescentCareDto(Page<AdolescentCare> adolescentCare) {
		return adolescentCare.map(i -> {
			// Conversion logic
			AdolescentCareDto covidSurveillanceDto = mapper.map(i, AdolescentCareDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(i);
			covidSurveillanceDto.setAudit(auditDto);
			return covidSurveillanceDto;
		});
	}

	private AuditDto buildAuditDto(AdolescentCare adolescentCare) {
		return AuditDto.builder().createdBy(adolescentCare.getCreatedBy()).modifiedBy(adolescentCare.getModifiedBy())
				.dateCreated(adolescentCare.getDateCreated()).dateModified(adolescentCare.getDateModified()).build();
	}
}
