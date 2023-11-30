package com.ssf.surveycube.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.surveycube.constant.SurveyType;
import com.ssf.surveycube.constant.Topics;
import com.ssf.surveycube.dtos.AuditDto;
import com.ssf.surveycube.dtos.FilterDto;
import com.ssf.surveycube.dtos.SurveyCubePageResponse;
import com.ssf.surveycube.entities.SurveyCube;
import com.ssf.surveycube.exception.EntityNotFoundException;
import com.ssf.surveycube.repository.SurveyCubeQueryRepository;
import com.ssf.surveycube.repository.SurveyCubeRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class SurveyCubeService implements ISurveyCubeService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SurveyCubeRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SurveyCubeQueryRepository queryRepository;

    @Override
    public SurveyCube createSurveyCube(SurveyCube cube) {
        cube.setAudit(getCreatedByAuditor());
        return repository.save(cube);
    }

    @Override
    public SurveyCube patchSurveyCube(String id, Map<String, Object> properties) {
        Optional<SurveyCube> optional = repository.findById(id);
        SurveyCube cube = null;
        if (optional.isPresent()) {
            cube = optional.get();
        } else {
            throw new NoSuchElementException();
        }

        Map<String, Object> cubeMap = objectMapper.convertValue(cube, Map.class);
        if (properties.containsKey("context")) {
            Map<String, Object> contextMap = objectMapper.convertValue(cube.getContext() != null ? cube.getContext() : new HashMap<>(), Map.class);
            contextMap.putAll((HashMap<String, String>) properties.get("context"));
            properties.remove("context");
            cubeMap.put("context", contextMap);
        }
        cubeMap.putAll(properties);
        cube = objectMapper.convertValue(cubeMap, SurveyCube.class);
        cube.setAudit(getModifiedByAuditor(cube.getAudit()));

        return repository.save(cube);
    }

    @Override
    public SurveyCube getSurveyCube(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id ::" + id + " not found"));
    }

    @Override
    public SurveyCubePageResponse getSurveyCubeByFilter(MultiValueMap<String, String> filters) {

        FilterDto filterDto = FilterDto.builder()
                .surveyType(SurveyType.valueOf(filters.get("surveyType").get(0).toString()))
                .topics(Topics.valueOf(filters.get("topics").get(0).toString()))
                .houseHoldId(filters.containsKey("houseHoldId") ? filters.get("houseHoldId") : null)
                .villageId(filters.containsKey("villageId") ? filters.get("villageId") : null)
                .citizenId(filters.containsKey("citizenId") ? filters.get("citizenId") : null)
                .build();

        filters.remove("villageId");
        filters.remove("houseHoldId");
        filters.remove("citizenId");
        filters.remove("surveyType");
        filters.remove("topics");
        filterDto.setFilters(filters);
        return queryRepository.getAllSurveyCube(filterDto);
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
}
