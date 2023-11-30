package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.AuditDTO;
import com.ssf.surveillance.dtos.LarvaVisitsFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Larva;
import com.ssf.surveillance.entities.LarvaVisits;
import com.ssf.surveillance.repository.LarvaRepository;
import com.ssf.surveillance.repository.LarvaVisitsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LarvaVisitsService implements ILarvaVisitsService {
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    private LarvaVisitsRepository larvaVisitsRepository;

    @Autowired
    private LarvaRepository larvaRepository;

    @Autowired
    private AuditorAware<String> auditorProvider;

    @Override
    public SurveillancePageResponse createLarvaSurveillanceVisits(String surveillanceId, LarvaVisits larvaVisits) {
        larvaVisits.setAudit(getCreatedByAuditor());
        Optional<Larva> larvaOptional = larvaRepository.findById(surveillanceId);
        if (!larvaOptional.isPresent() && !surveillanceId.equals(larvaVisits.getLarvaSurveillanceId())) {
            throw new NoSuchElementException();
        }
        larvaVisits = larvaVisitsRepository.save(larvaVisits);
        log.info("Larva Visit Created :: {}", larvaVisits.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(larvaVisits)).totalElements(1L).build();

    }

    @Override
    public SurveillancePageResponse getLarvaSurveillanceVisits(String surveillanceId, String visitId) {
        List<LarvaVisits> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<LarvaVisits> larvaVisitsOptional = larvaVisitsRepository.findByIdAndLarvaSurveillanceId(visitId, surveillanceId);
        if (larvaVisitsOptional.isPresent()) {
            totalPages = 1L;
            results.add(larvaVisitsOptional.get());
            totalElements = Long.valueOf(results.size());
        }
        return SurveillancePageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();
    }

    @Override
    public SurveillancePageResponse updateLarvaSurveillanceVisits(String surveillanceId, String visitId, HashMap<String, Object> properties) {
        Optional<LarvaVisits> larvaVisitsOptional = larvaVisitsRepository.findByIdAndLarvaSurveillanceId(visitId, surveillanceId);
        LarvaVisits larvaVisits;
        Optional<Larva> larvaOptional = larvaRepository.findById(surveillanceId);
        if (!larvaOptional.isPresent()) {
            throw new NoSuchElementException();
        }
        if (larvaVisitsOptional.isPresent()) {
            larvaVisits = larvaVisitsOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        Map<String, Object> larvaVisitsMap = objectMapper.convertValue(larvaVisits, Map.class);
        larvaVisitsMap.putAll(properties);
        larvaVisits = objectMapper.convertValue(larvaVisitsMap, LarvaVisits.class);
        larvaVisits.setAudit(getModifiedByAuditor(larvaVisits.getAudit()));
        larvaVisits = larvaVisitsRepository.save(larvaVisits);
        log.info("Larva Visit Updated :: {}", larvaVisits.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(larvaVisits)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getLarvaSurveillanceVisitsByFilter(LarvaVisitsFilterDTO larvaVisitsFilterDTO) {

        Pageable paging = PageRequest.of(larvaVisitsFilterDTO.getPage(), larvaVisitsFilterDTO.getSize());
        LarvaVisits example = new LarvaVisits();
        Page<LarvaVisits> larvaVisitsPage;
        example.setLarvaSurveillanceId(larvaVisitsFilterDTO.getSurveillanceId());
        larvaVisitsPage = larvaVisitsRepository.findAll(Example.of(example), paging);
        return SurveillancePageResponse.builder().totalPages(Long.valueOf(larvaVisitsPage.getTotalPages())).totalElements(larvaVisitsPage.getTotalElements()).content(larvaVisitsPage.getContent()).build();
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
