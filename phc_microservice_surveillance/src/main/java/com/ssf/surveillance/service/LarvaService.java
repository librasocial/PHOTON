package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.AuditDTO;
import com.ssf.surveillance.dtos.LarvaFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Larva;
import com.ssf.surveillance.repository.LarvaRepository;
import com.ssf.surveillance.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LarvaService implements ILarvaService {
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    private LarvaRepository larvaRepository;
    @Autowired
    private AuditorAware<String> auditorProvider;

    @Override
    public SurveillancePageResponse createLarvaSurveillance(Larva larva) {
        larva.setAudit(getCreatedByAuditor());
        larva = larvaRepository.save(larva);
        log.info("Larva Created :: {}", larva.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(larva)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getLarvaSurveillance(String surveillanceId) {
        List<Larva> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<Larva> LarvaOptional = larvaRepository.findById(surveillanceId);
        if (LarvaOptional.isPresent()) {
            totalPages = 1L;
            results.add(LarvaOptional.get());
            totalElements = Long.valueOf(results.size());
        }
        return SurveillancePageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();
    }

    @Override
    public SurveillancePageResponse updateLarvaSurveillance(String surveillanceId, HashMap<String, Object> properties) {
        Optional<Larva> larvaOptional = larvaRepository.findById(surveillanceId);
        Larva larva;
        if (larvaOptional.isPresent()) {
            larva = larvaOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        Map<String, Object> larvaMap = objectMapper.convertValue(larva, Map.class);
        larvaMap.putAll(properties);
        larva = objectMapper.convertValue(larvaMap, Larva.class);
        larva.setAudit(getModifiedByAuditor(larva.getAudit()));
        larva = larvaRepository.save(larva);
        log.info("Larva Updated :: {}", larva.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(larva)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getLarvaSurveillanceByFilter(LarvaFilterDTO larvaFilterDTO) {

        Pageable paging = PageRequest.of(larvaFilterDTO.getPage(), larvaFilterDTO.getSize());
        Larva example = new Larva();
        Page<Larva> larvaPage;
        if (larvaFilterDTO.getDateOfSurvey().isPresent()) {
            example.setDateOfSurveillance(Utils.stringToLocalDate(larvaFilterDTO.getDateOfSurvey().get()));
        }
        if (larvaFilterDTO.getHouseholdId().isPresent()) {
            example.setHouseholdId(larvaFilterDTO.getHouseholdId().get());
        }
        if (larvaFilterDTO.getVillageId().isPresent()) {
            example.setVillageId(larvaFilterDTO.getVillageId().get());
        }
        if (larvaFilterDTO.getPlaceType().isPresent()) {
            example.setPlaceType(larvaFilterDTO.getPlaceType().get());
        }
        if (larvaFilterDTO.getPlaceOrgId().isPresent()) {
            example.setPlaceOrgId(larvaFilterDTO.getPlaceOrgId().get());
        }
        larvaPage = larvaRepository.findAll(Example.of(example), paging);
        return SurveillancePageResponse.builder().totalPages(Long.valueOf(larvaPage.getTotalPages())).totalElements(larvaPage.getTotalElements()).content(larvaPage.getContent()).build();
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
