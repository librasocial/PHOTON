package com.ssf.childcareimmunization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.childcareimmunization.constants.Constants;
import com.ssf.childcareimmunization.dtos.*;
import com.ssf.childcareimmunization.entities.Immunization;
import com.ssf.childcareimmunization.exception.InvalidInputException;
import com.ssf.childcareimmunization.kafka.KafkaProducer;
import com.ssf.childcareimmunization.kafka.message.Actor;
import com.ssf.childcareimmunization.kafka.message.Context;
import com.ssf.childcareimmunization.kafka.message.KafkaMessage;
import com.ssf.childcareimmunization.kafka.topic.Topics;
import com.ssf.childcareimmunization.repository.ChildCareImmunizationRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImmunizationService {
	@Autowired
	ChildCareImmunizationRepo cCImmunizationRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	public AuditorAware<String> auditorProvider;

	@Autowired
	private KafkaProducer kafkaProducer;

	public ImmunizationDto createImmunization(ImmunizationDto request) throws InvalidInputException {
		Immunization immunization = mapper.map(request, Immunization.class);
		immunization = cCImmunizationRepo.insert(immunization);
		ImmunizationDto response = mapper.map(immunization, ImmunizationDto.class);
		KafkaMessage<ImmunizationDto> kafkaMessage = new KafkaMessage<>();
		kafkaMessage.setContext(Context.builder().language("en")
				.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
		kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
		kafkaMessage.setType(Topics.ChildCareImmunization + "Created");
		kafkaMessage.setObject(response);
		kafkaProducer.sendMessage(Topics.ChildCareImmunization.toString(), kafkaMessage);
		return response;
	}

	public ImmunizationDto getImmunization(String immunizationId) throws InvalidInputException {
		Optional<Immunization> immunizationDtoFromDb = cCImmunizationRepo.findById(immunizationId);
		if (immunizationDtoFromDb.isPresent()) {
			ImmunizationDto response = mapper.map(immunizationDtoFromDb.get(), ImmunizationDto.class);
			return response;
		} else {
			throw new InvalidInputException("Immunization is not available for given ID");
		}
	}

	public ImmunizationDto updateImmunization(String immunizationId, Map<String, Object> request)
			throws InvalidInputException {
		Optional<Immunization> immunizationFromDb = cCImmunizationRepo.findById(immunizationId);
		if (immunizationFromDb.isPresent()) {
			Map<String, Object> dbMap = objectMapper.convertValue(immunizationFromDb.get(), Map.class);
			dbMap.putAll(request);
			Immunization data = objectMapper.convertValue(dbMap, Immunization.class);
			Immunization updatedImmunization = cCImmunizationRepo.save(data);
			ImmunizationDto response = mapper.map(updatedImmunization, ImmunizationDto.class);

			KafkaMessage<ImmunizationDto> kafkaMessage = new KafkaMessage<>();
			kafkaMessage.setContext(Context.builder().language("en")
					.authToken(this.request.getHeader(Constants.AUTHORIZATION_HEADER)).build());
			kafkaMessage.setActor(Actor.builder().id(this.request.getHeader(Constants.XUSER_ID_HEADER)).build());
			kafkaMessage.setType(Topics.ChildCareImmunization + "Updated");
			kafkaMessage.setObject(response);
			kafkaProducer.sendMessage(Topics.ChildCareImmunization.toString(), kafkaMessage);
			return response;
		} else {
			throw new InvalidInputException("Immunization is not available for given ID");
		}
	}

	public PageResponse getImmunizationByFilter(String immunizationId, String childCitizenId, Integer page,
			Integer size) {
		log.debug("filter the Immunization by childCitizenId {} ", childCitizenId);

		auditorProvider.getCurrentAuditor();
		Pageable paging = PageRequest.of(page, size);
		Immunization example = Immunization.builder().build();

		if (StringUtils.isNotBlank(immunizationId)) {
			example.setId(immunizationId);
		}

		if (StringUtils.isNotBlank(childCitizenId)) {
			example.setChildCitizenId(childCitizenId);
		}

		return buildImmunizationPageResponse(cCImmunizationRepo.findAll(Example.of(example), paging));
	}

	private PageResponse buildImmunizationPageResponse(Page<Immunization> immunization) {
		if (immunization == null || immunization.isEmpty()) {
			log.debug(" Pagination result is null ");
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
					.data(new ArrayList<>()).build();

		} else {
			log.debug(" Pagination result is returned " + immunization.getTotalElements());
			Page<ImmunizationDto> infantDtoPage = buildImmunizationDto(immunization);
			return PageResponse.builder()
					.meta(PageDto.builder().totalPages((long) immunization.getTotalPages())
							.totalElements(immunization.getTotalElements()).number(immunization.getNumber())
							.size(immunization.getSize()).build())
					.data(infantDtoPage.toList()).build();
		}
	}

	private Page<ImmunizationDto> buildImmunizationDto(Page<Immunization> immunization) {
		return immunization.map(i -> {
			// Conversion logic
			ImmunizationDto immunizationDto = mapper.map(i, ImmunizationDto.class);
			// Audit save
			AuditDto auditDto = buildAuditDto(i);
			immunizationDto.setAudit(auditDto);
			return immunizationDto;
		});
	}

	private AuditDto buildAuditDto(Immunization immunization) {
		return AuditDto.builder().createdBy(immunization.getAudit().getCreatedBy())
				.modifiedBy(immunization.getAudit().getModifiedBy())
				.dateCreated(immunization.getAudit().getDateCreated())
				.dateModified(immunization.getAudit().getDateModified()).build();
	}

	public LinkedHashMap<String, List<ImmunizationGroupByDto>> getGroupedDose(String ccitizenId) throws InvalidInputException {
		List<Immunization> immunizationList = new ArrayList<>();

		immunizationList = cCImmunizationRepo.findAllByChildCitizenId(ccitizenId);

		List<ImmunizationGroupByDto> immunizationGroupByDtoList = new ArrayList<>();
		List<VaccinesDto> vaccines = new ArrayList<>();
		for (Immunization immunizationFromList : immunizationList) {
			for (VaccinesDto vaccinesDto : immunizationFromList.getVaccines()) {
				ImmunizationGroupByDto group = new ImmunizationGroupByDto();
				group.setDoseNumber(immunizationFromList.getDoseNumber());
				group.setImmunizationSubGroup(ImmunizationSubGroup.builder()
						.name(vaccinesDto.getName())
						.batchNumber(vaccinesDto.getImmunization() != null ? vaccinesDto.getImmunization().getBatchNumber() : null)
						.ashaWorker(immunizationFromList.getAshaWorker())
						.expirationDate(vaccinesDto.getImmunization() != null ? vaccinesDto.getImmunization().getExpirationDate() : null)
						.manufacturer(vaccinesDto.getImmunization() != null ? vaccinesDto.getImmunization().getManufacturer() : null)
						.vaccinatedDate(vaccinesDto.getImmunization() != null ? vaccinesDto.getImmunization().getVaccinatedDate() : null)
						.earliestDueDate(vaccinesDto.getEarliestDueDate())
						.aefiReason(immunizationFromList.getAefiReason())
						.aefiStatus(immunizationFromList.getAefiStatus())
						.isCovidResultPositive(immunizationFromList.getIsCovidResultPositive())
						.build());
				immunizationGroupByDtoList.add(group);
			}
		}
		Map<String, List<ImmunizationGroupByDto>> groupByDoseMap = immunizationGroupByDtoList.stream()
				.collect(Collectors.groupingBy(ImmunizationGroupByDto::getDoseNumber));
		LinkedHashMap<String, List<ImmunizationGroupByDto>> orderdDoseMap = new LinkedHashMap<>();
		if (groupByDoseMap.containsKey("Birth Dose")) {
			orderdDoseMap.put("Birth Dose", groupByDoseMap.get("Birth Dose"));
		}
		if (groupByDoseMap.containsKey("Week 6")) {
			orderdDoseMap.put("Week 6", groupByDoseMap.get("Week 6"));
		}
		if (groupByDoseMap.containsKey("Week 10")) {
			orderdDoseMap.put("Week 10", groupByDoseMap.get("Week 10"));
		}
		if (groupByDoseMap.containsKey("Week 14")) {
			orderdDoseMap.put("Week 14", groupByDoseMap.get("Week 14"));
		}
		if (groupByDoseMap.containsKey("Month 9-12")) {
			orderdDoseMap.put("Month 9-12", groupByDoseMap.get("Month 9-12"));
		}
		if (groupByDoseMap.containsKey("Month 16-24")) {
			orderdDoseMap.put("Month 16-24", groupByDoseMap.get("Month 16-24"));
		}
		if (groupByDoseMap.containsKey("Month 24-60")) {
			orderdDoseMap.put("Month 24-60", groupByDoseMap.get("Month 24-60"));
		}
		if (groupByDoseMap.containsKey("Year 5-6")) {
			orderdDoseMap.put("Year 5-6", groupByDoseMap.get("Year 5-6"));
		}
		if (groupByDoseMap.containsKey("Year 7-10")) {
			orderdDoseMap.put("Year 7-10", groupByDoseMap.get("Year 7-10"));
		}

		return orderdDoseMap;
	}

	public String createImmunizationList(List<ImmunizationDto> immunizationList) throws InvalidInputException {
		for (ImmunizationDto immunizationDto : immunizationList) {
			Immunization immunization = mapper.map(immunizationDto, Immunization.class);
			immunization = cCImmunizationRepo.save(immunization);
		}

		return "The list of Immunization is created successfully";
	}

	public ImmunizationDto getImmunizationByChildId(String childCitizenId) throws InvalidInputException {
		List<Immunization> immunizationListFromDb = cCImmunizationRepo.findByChildCitizenIdOrderById(childCitizenId);
		ImmunizationDto response = null;
		for (Immunization immunizationFromDb : immunizationListFromDb) {
			List<VaccinesDto> pendingVaccinesList = immunizationFromDb.getVaccines().stream()
					.filter(v -> v.getImmunization() == null).collect(Collectors.toList());
			if (pendingVaccinesList.size() > 0) {
				response = mapper.map(immunizationFromDb, ImmunizationDto.class);
				break;
			}
		}
		if (response != null) {
			return response;
		} else {
			throw new InvalidInputException("Immunization is not available for given ID");
		}

	}

}
