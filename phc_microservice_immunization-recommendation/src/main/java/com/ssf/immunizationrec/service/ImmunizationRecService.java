package com.ssf.immunizationrec.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.immunizationrec.dtos.*;
import com.ssf.immunizationrec.entities.ImmunizationRec;
import com.ssf.immunizationrec.exception.EntityNotFoundException;
import com.ssf.immunizationrec.repository.IImmunizationRecRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ImmunizationRecService {

    @Autowired
    private IImmunizationRecRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuditorAware<String> auditorProvider;

    public ImmunizationRecDto createImmunRec(ImmunizationRecDto request) {
        log.debug("Creating the Immunization Rec ");

        ImmunizationRec immunizationRec = mapper.map(request, ImmunizationRec.class);
        repository.save(immunizationRec);
        ImmunizationRecDto response = mapper.map(immunizationRec, ImmunizationRecDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(immunizationRec);
        response.setAudit(auditDto);
        return response;
    }

    public ImmunizationRecDto readImmunRec(String id) {
        log.debug("Reading the PNC for service {}", id);
        auditorProvider.getCurrentAuditor();
        ImmunizationRec immunizationRec = getImmunRec(id);
        ImmunizationRecDto response = mapper.map(immunizationRec, ImmunizationRecDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(immunizationRec);
        response.setAudit(auditDto);

        return response;

    }

    public PageResponse getImmunRecByFilter(FilterDto filterDto) {
        log.debug("filter the Immunrec by inputs {} ", filterDto.toString());
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize());
        ImmunizationRec example = ImmunizationRec.builder().build();

        if (StringUtils.isNotBlank(filterDto.getCitizenId())) {
            example.setCitizenId(filterDto.getCitizenId());
        }

        if (StringUtils.isNotBlank(filterDto.getPatientId())) {
            example.setPatientId(filterDto.getPatientId());
        }

        if (StringUtils.isNotBlank(filterDto.getUhid())) {
            example.setUhid(filterDto.getUhid());
        }

        if (filterDto.getRecommendedDate()!=null) {
            example.setRecommendedDate(filterDto.getRecommendedDate());
        }

        return buildImmunRecPageResponse(repository.findAll(Example.of(example), paging));
    }

    public ImmunizationRecDto patchImmunRec(String id, Map<String, Object> request) {
        log.debug("Updating the ImmunRec for rec id {}", id);
        ImmunizationRecDto immunizationRecDto = objectMapper.convertValue(request, ImmunizationRecDto.class);

        ImmunizationRec existingImmunRec = getImmunRec(id);
        // copy the fields which are changed to entity object
        Map<String, Object> dbMap = objectMapper.convertValue(existingImmunRec, Map.class);
        List<RecommendationsDto> oldRec = new ArrayList<>();

        if (request.containsKey("recommendations")) {
            List<RecommendationsDto> newRecDto = (List<RecommendationsDto>) request.get("recommendations");
            if (dbMap.containsKey("recommendations") && dbMap.get("recommendations") != null) {
                oldRec = (List<RecommendationsDto>) dbMap.get("recommendations");
            }
            oldRec.addAll(newRecDto);
            request.put("recommendations", oldRec);
        }

        dbMap.putAll(request);
        ImmunizationRec data = objectMapper.convertValue(dbMap, ImmunizationRec.class);
        repository.save(data);

        //response generate
        var response = mapper.map(data, ImmunizationRecDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(existingImmunRec);
        response.setAudit(auditDto);
        return response;
    }

    public ImmunizationRec getImmunRec(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("immuneRecId ::" + id + " not found"));
    }

    private PageResponse buildImmunRecPageResponse(Page<ImmunizationRec> immunizationRec) {
        if (immunizationRec == null || immunizationRec.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + immunizationRec.getTotalElements());
            Page<ImmunizationRecDto> infantDtoPage = buildPImmunRecDtos(immunizationRec);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) immunizationRec.getTotalPages())
                            .totalElements(immunizationRec.getTotalElements())
                            .number(immunizationRec.getNumber())
                            .size(immunizationRec.getSize())
                            .build())
                    .data(infantDtoPage.toList())
                    .build();
        }
    }

    private Page<ImmunizationRecDto> buildPImmunRecDtos(Page<ImmunizationRec> infant) {
        return infant.map(i -> {
            // Conversion logic
            ImmunizationRecDto infantDto = mapper.map(i, ImmunizationRecDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(i);
            infantDto.setAudit(auditDto);
            return infantDto;
        });
    }
    
    private AuditDto buildAuditDto(ImmunizationRec immunizationRec) {
        return AuditDto.builder().createdBy(immunizationRec.getCreatedBy())
                .modifiedBy(immunizationRec.getModifiedBy())
                .dateCreated(immunizationRec.getDateCreated())
                .dateModified(immunizationRec.getDateModified())
                .build();
    }
}
