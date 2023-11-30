package com.ssf.adolescentcareservice.service;

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
import com.ssf.adolescentcareservice.constants.Constants;
import com.ssf.adolescentcareservice.dtos.AdolescentCareServiceDto;
import com.ssf.adolescentcareservice.dtos.AuditDto;
import com.ssf.adolescentcareservice.dtos.PageDto;
import com.ssf.adolescentcareservice.dtos.PageResponse;
import com.ssf.adolescentcareservice.entities.AdolescentCareService;
import com.ssf.adolescentcareservice.exception.InvalidInputException;
import com.ssf.adolescentcareservice.kafka.KafkaProducer;
import com.ssf.adolescentcareservice.kafka.message.Actor;
import com.ssf.adolescentcareservice.kafka.message.Context;
import com.ssf.adolescentcareservice.kafka.message.KafkaMessage;
import com.ssf.adolescentcareservice.kafka.topic.Topics;
import com.ssf.adolescentcareservice.repository.AdolescentCareServiceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdolescentCareServiceService {

	@Autowired
	AdolescentCareServiceRepository adolescentcareserviceRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	public AuditorAware<String> auditorProvider;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Value("${kafka.enabled}")
	private boolean kafkaEnabled;

	public AdolescentCareServiceDto createAdolescentCareService(AdolescentCareServiceDto request) {
		AdolescentCareService adolescentCareService = mapper.map(request, AdolescentCareService.class);
		adolescentCareService = adolescentcareserviceRepo.insert(adolescentCareService);
		AdolescentCareServiceDto response = mapper.map(adolescentCareService, AdolescentCareServiceDto.class);

		// Audit save
		AuditDto auditDto = buildAuditDto(adolescentCareService);
		response.setAudit(auditDto);

		if (kafkaEnabled) {
			KafkaMessage<AdolescentCareServiceDto> kafkaMessage = new KafkaMessage<>();
			kafkaMessage.setContext(Context.builder().language("en")
					.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
			kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
			kafkaMessage.setType(Topics.AdolescentCareService + "Created");
			kafkaMessage.setObject(response);
			kafkaProducer.sendMessage(Topics.AdolescentCareService.toString(), kafkaMessage);
		}

		return response;
	}

	public AdolescentCareServiceDto getAdolescentCareService(String adolescentCareServiceId)
			throws InvalidInputException {
		Optional<AdolescentCareService> adolescentCareServiceFromDb = adolescentcareserviceRepo
				.findById(adolescentCareServiceId);
		if (adolescentCareServiceFromDb.isPresent()) {
			AdolescentCareServiceDto response = mapper.map(adolescentCareServiceFromDb.get(),
					AdolescentCareServiceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(adolescentCareServiceFromDb.get());
			response.setAudit(auditDto);
			return response;
		} else {
			throw new InvalidInputException("AdolescentCareService is not available for given ID");
		}
	}

	public AdolescentCareServiceDto updateAdolescentCareService(String adolescentCareServiceId,
			Map<String, Object> request) throws InvalidInputException {
		Optional<AdolescentCareService> adolescentCareServiceFromDb = adolescentcareserviceRepo
				.findById(adolescentCareServiceId);
		if (adolescentCareServiceFromDb.isPresent()) {

			Map<String, Object> dbMap = objectMapper.convertValue(adolescentCareServiceFromDb.get(), Map.class);
			dbMap.putAll(request);
			AdolescentCareService data = objectMapper.convertValue(dbMap, AdolescentCareService.class);
			AdolescentCareService updatedAdolescentCareService = adolescentcareserviceRepo.save(data);
			AdolescentCareServiceDto response = mapper.map(updatedAdolescentCareService,
					AdolescentCareServiceDto.class);

			// Audit save
			AuditDto auditDto = buildAuditDto(adolescentCareServiceFromDb.get());
			response.setAudit(auditDto);
			
			if (kafkaEnabled) {
				KafkaMessage<AdolescentCareServiceDto> kafkaMessage = new KafkaMessage<>();
				kafkaMessage.setContext(Context.builder().language("en")
						.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
				kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
				kafkaMessage.setType(Topics.AdolescentCareService + "Updated");
				kafkaMessage.setObject(response);
				kafkaProducer.sendMessage(Topics.AdolescentCareService.toString(), kafkaMessage);
			}
			return response;
		} else {
			throw new InvalidInputException("AdolescentCareService is not available for given ID");
		}
	}

	private AuditDto buildAuditDto(AdolescentCareService adolescentCareService) {
		return AuditDto.builder().createdBy(adolescentCareService.getCreatedBy())
				.modifiedBy(adolescentCareService.getModifiedBy()).dateCreated(adolescentCareService.getDateCreated())
				.dateModified(adolescentCareService.getDateModified()).build();
	}

	public PageResponse getAdolescentCareServiceByFilter(String childCitizenId, Integer page, Integer size) {
		log.debug("filter the CovidSurveillance by citizenId {} ", childCitizenId);

		auditorProvider.getCurrentAuditor();
		Pageable paging = PageRequest.of(page, size);
		AdolescentCareService example = AdolescentCareService.builder().build();

		if (StringUtils.isNotBlank(childCitizenId)) {
			example.setChildCitizenId(childCitizenId);
		}

		return buildAdolescentCareServicePageResponse(adolescentcareserviceRepo.findAll(Example.of(example), paging));
	}

	private PageResponse buildAdolescentCareServicePageResponse(Page<AdolescentCareService> adolescentCareService) {
		if (adolescentCareService == null || adolescentCareService.isEmpty()) {
			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
			log.debug(" Pagination result is returned " + adolescentCareService.getTotalElements());
			Page<AdolescentCareServiceDto> infantDtoPage = buildAdolescentCareServiceDto(adolescentCareService);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) adolescentCareService.getTotalPages())
							.totalElements(adolescentCareService.getTotalElements())
							.number(adolescentCareService.getNumber()).size(adolescentCareService.getSize()).build())
					.data(infantDtoPage.toList()).build();
		}
	}

	private Page<AdolescentCareServiceDto> buildAdolescentCareServiceDto(
			Page<AdolescentCareService> adolescentCareService) {
		return adolescentCareService.map(i -> {
			// Conversion logic
			AdolescentCareServiceDto adolescentCareServiceDto = mapper.map(i, AdolescentCareServiceDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(i);
			adolescentCareServiceDto.setAudit(auditDto);
			return adolescentCareServiceDto;
		});
	}

}
