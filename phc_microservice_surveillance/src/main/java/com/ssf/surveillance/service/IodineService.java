package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.AuditDTO;
import com.ssf.surveillance.dtos.IodineFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Iodine;
import com.ssf.surveillance.repository.IodineRepository;
import com.ssf.surveillance.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class IodineService implements IIodineService {
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    private IodineRepository iodineRepository;
    @Autowired
    private AuditorAware<String> auditorProvider;

    @Override
    public SurveillancePageResponse createIodineSurveillance(Iodine iodine) {
        iodine.setAudit(getCreatedByAuditor());
        iodine = iodineRepository.save(iodine);
        log.info("Iodine Created :: {}", iodine.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(iodine)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getIodineSurveillance(String surveillanceId) {
        List<Iodine> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<Iodine> iodineOptional = iodineRepository.findById(surveillanceId);
        if (iodineOptional.isPresent()) {
            totalPages = 1L;
            results.add(iodineOptional.get());
            totalElements = Long.valueOf(results.size());
        }
        return SurveillancePageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();
    }

    @Override
    public SurveillancePageResponse updateIodineSurveillance(String surveillanceId, HashMap<String, Object> properties) {
        Optional<Iodine> iodineOptional = iodineRepository.findById(surveillanceId);
        Iodine iodine;
        if (iodineOptional.isPresent()) {
            iodine = iodineOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        Map<String, Object> iodineMap = objectMapper.convertValue(iodine, Map.class);
        iodineMap.putAll(properties);
        iodine = objectMapper.convertValue(iodineMap, Iodine.class);
        iodine.setAudit(getModifiedByAuditor(iodine.getAudit()));
        iodine = iodineRepository.save(iodine);
        log.info("Iodine Updated :: {}", iodine.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(iodine)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getIodineSurveillanceByFilter(IodineFilterDTO iodineFilterDTO) {

        Pageable paging = PageRequest.of(iodineFilterDTO.getPage(), iodineFilterDTO.getSize());
        Iodine example = new Iodine();
        Page<Iodine> iodinePage;
        if (iodineFilterDTO.getDateOfSurvey().isPresent()) {
            example.setDateOfSurveillance(Utils.stringToLocalDate(iodineFilterDTO.getDateOfSurvey().get()));
        }
        if (iodineFilterDTO.getHouseholdId().isPresent()) {
            example.setHouseholdId(iodineFilterDTO.getHouseholdId().get());
        }
        if (iodineFilterDTO.getVillageId().isPresent()) {
            example.setVillageId(iodineFilterDTO.getVillageId().get());
        }
        if (iodineFilterDTO.getPlaceType().isPresent()) {
            example.setPlaceType(iodineFilterDTO.getPlaceType().get());
        }
        if (iodineFilterDTO.getPlaceOrgId().isPresent()) {
            example.setPlaceOrgId(iodineFilterDTO.getPlaceOrgId().get());
        }
        iodinePage = iodineRepository.findAll(Example.of(example), paging);
        return SurveillancePageResponse.builder().totalPages(Long.valueOf(iodinePage.getTotalPages())).totalElements(iodinePage.getTotalElements()).content(iodinePage.getContent()).build();
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
