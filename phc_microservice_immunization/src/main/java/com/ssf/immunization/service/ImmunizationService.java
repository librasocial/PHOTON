package com.ssf.immunization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.immunization.dtos.*;
import com.ssf.immunization.entities.Immunization;
import com.ssf.immunization.exception.EntityNotFoundException;
import com.ssf.immunization.repository.IImmunizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class ImmunizationService {

    @Autowired
    private IImmunizationRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuditorAware<String> auditorProvider;

    public ImmunizationDto createImmun(ImmunizationDto request) {
        log.debug("Creating the Immunization");

        Immunization immunization = mapper.map(request, Immunization.class);
        repository.save(immunization);
        ImmunizationDto response = mapper.map(immunization, ImmunizationDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(immunization);
        response.setAudit(auditDto);
        return response;
    }

    public ImmunizationDto readImmun(String id) {
        log.debug("Reading the PNC for service {}", id);
        auditorProvider.getCurrentAuditor();
        Immunization immunization = getImmun(id);
        ImmunizationDto response = mapper.map(immunization, ImmunizationDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(immunization);
        response.setAudit(auditDto);

        return response;

    }

    public PageResponse getImmunByFilter(FilterDto filterDto) {
        log.debug("filter the Immun by inputs {} ", filterDto.toString());
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize());
        Immunization example = Immunization.builder().build();

        if (filterDto.getRecommendationId()!=null) {
            example.setRecommendationId(filterDto.getRecommendationId());
        }

        if (StringUtils.isNotBlank(filterDto.getCitizenId())) {
            example.setCitizenId(filterDto.getCitizenId());
        }

        if (StringUtils.isNotBlank(filterDto.getPatientId())) {
            example.setPatientId(filterDto.getPatientId());
        }

        if (StringUtils.isNotBlank(filterDto.getUhid())) {
            example.setUhid(filterDto.getUhid());
        }

        return buildImmunPageResponse(repository.findAll(Example.of(example), paging));
    }

    public ImmunizationDto patchImmun(String id, Map<String, Object> request) {
        log.debug("Updating the Immun for  id {}", id);
        ImmunizationDto immunizationDto = objectMapper.convertValue(request, ImmunizationDto.class);

        Immunization existingImmun = getImmun(id);
        // copy the fields which are changed to entity object
        Map<String, Object> dbMap = objectMapper.convertValue(existingImmun, Map.class);
        dbMap.putAll(request);
        Immunization data = objectMapper.convertValue(dbMap, Immunization.class);
        repository.save(data);

        //response generate
        var response = mapper.map(data, ImmunizationDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(existingImmun);
        response.setAudit(auditDto);
        return response;
    }

    public Immunization getImmun(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("immuneId ::" + id + " not found"));
    }

    private PageResponse buildImmunPageResponse(Page<Immunization> immunization) {
        if (immunization == null || immunization.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + immunization.getTotalElements());
            Page<ImmunizationDto> infantDtoPage = buildPImmunDtos(immunization);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) immunization.getTotalPages())
                            .totalElements(immunization.getTotalElements())
                            .number(immunization.getNumber())
                            .size(immunization.getSize())
                            .build())
                    .data(infantDtoPage.toList())
                    .build();
        }
    }

    private Page<ImmunizationDto> buildPImmunDtos(Page<Immunization> infant) {
        return infant.map(i -> {
            // Conversion logic
            ImmunizationDto infantDto = mapper.map(i, ImmunizationDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(i);
            infantDto.setAudit(auditDto);
            return infantDto;
        });
    }
    
    private AuditDto buildAuditDto(Immunization immunization) {
        return AuditDto.builder().createdBy(immunization.getCreatedBy())
                .modifiedBy(immunization.getModifiedBy())
                .dateCreated(immunization.getDateCreated())
                .dateModified(immunization.getDateModified())
                .build();
    }
}
