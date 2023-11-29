package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.AuditDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.dtos.WaterSamplesFilterDTO;
import com.ssf.surveillance.entities.WaterSamples;
import com.ssf.surveillance.repository.WaterSamplesRepository;
import com.ssf.surveillance.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class WaterSamplesService implements IWaterSamplesService {
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    private WaterSamplesRepository waterSamplesRepository;
    @Autowired
    private AuditorAware<String> auditorProvider;

    @Override
    public SurveillancePageResponse createWaterSamplesSurveillance(WaterSamples waterSamples) {
        waterSamples.setAudit(getCreatedByAuditor());
        waterSamples = waterSamplesRepository.save(waterSamples);
        log.info("Water Samples Created :: {}", waterSamples.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(waterSamples)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getWaterSamplesSurveillance(String surveillanceId) {
        List<WaterSamples> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<WaterSamples> waterSamplesOptional = waterSamplesRepository.findById(surveillanceId);
        if (waterSamplesOptional.isPresent()) {
            totalPages = 1L;
            results.add(waterSamplesOptional.get());
            totalElements = Long.valueOf(results.size());
        }
        return SurveillancePageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();

    }

    @Override
    public SurveillancePageResponse updateWaterSamplesSurveillance(String surveillanceId, HashMap<String, Object> properties) {
        Optional<WaterSamples> waterSamplesOptional = waterSamplesRepository.findById(surveillanceId);
        WaterSamples waterSamples;
        if (waterSamplesOptional.isPresent()) {
            waterSamples = waterSamplesOptional.get();
        } else {
            throw new NoSuchElementException();
        }

        Map<String, Object> waterSamplesMap = objectMapper.convertValue(waterSamples, Map.class);
        if (properties.containsKey("sample")) {
            Map<String, Object> sampleMap = objectMapper.convertValue(waterSamples.getSample() != null ? waterSamples.getSample() : new HashMap<>(), Map.class);
            sampleMap.putAll((HashMap<String, String>) properties.get("sample"));
            properties.remove("sample");
            waterSamplesMap.put("sample", sampleMap);
        }
        if (properties.containsKey("testResult")) {
            Map<String, Object> testResultMap = objectMapper.convertValue(waterSamples.getTestResult() != null ? waterSamples.getTestResult() : new HashMap<>(), Map.class);
            testResultMap.putAll((HashMap<String, String>) properties.get("testResult"));
            properties.remove("testResult");
            waterSamplesMap.put("testResult", testResultMap);
        }
        waterSamplesMap.putAll(properties);
        waterSamples = objectMapper.convertValue(waterSamplesMap, WaterSamples.class);
        waterSamples.setAudit(getModifiedByAuditor(waterSamples.getAudit()));
        waterSamples = waterSamplesRepository.save(waterSamples);
        log.info("Water Samples Updated :: {}", waterSamples.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(waterSamples)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getWaterSamplesSurveillanceByFilter(WaterSamplesFilterDTO waterSamplesFilterDTO) {

        Pageable paging = PageRequest.of(waterSamplesFilterDTO.getPage(), waterSamplesFilterDTO.getSize());
        WaterSamples example = new WaterSamples();
        Page<WaterSamples> waterSamplesPage;
        if (waterSamplesFilterDTO.getDateOfSurvey().isPresent()) {
            example.setDateOfSurveillance(Utils.stringToLocalDate(waterSamplesFilterDTO.getDateOfSurvey().get()));
        }
        if (waterSamplesFilterDTO.getHouseholdId().isPresent()) {
            example.setHouseholdId(waterSamplesFilterDTO.getHouseholdId().get());
        }
        if (waterSamplesFilterDTO.getVillageId().isPresent()) {
            example.setVillageId(waterSamplesFilterDTO.getVillageId().get());
        }
        if (waterSamplesFilterDTO.getPlaceType().isPresent()) {
            example.setPlaceType(waterSamplesFilterDTO.getPlaceType().get());
        }
        if (waterSamplesFilterDTO.getPlaceOrgId().isPresent()) {
            example.setPlaceOrgId(waterSamplesFilterDTO.getPlaceOrgId().get());
        }
        waterSamplesPage = waterSamplesRepository.findAll(Example.of(example), paging);
        return SurveillancePageResponse.builder().totalPages(Long.valueOf(waterSamplesPage.getTotalPages())).totalElements(waterSamplesPage.getTotalElements()).content(waterSamplesPage.getContent()).build();
    }

    private AuditDTO getCreatedByAuditor() {
        String username = "";
        Optional<String> optional = auditorProvider.getCurrentAuditor();
        if (optional.isPresent()) {
            username = optional.get();
        }
        return AuditDTO.builder().modifiedBy(username).createdBy(username).build();
    }

    private AuditDTO getModifiedByAuditor(AuditDTO auditDTO) {
        String username = "";
        Optional<String> optional = auditorProvider.getCurrentAuditor();
        if (optional.isPresent()) {
            username = optional.get();
        }
        auditDTO.setModifiedBy(username);
        return auditDTO;
    }

}
