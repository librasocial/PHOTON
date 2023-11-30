package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.AuditDTO;
import com.ssf.surveillance.dtos.IodineSamplesFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Iodine;
import com.ssf.surveillance.entities.IodineSamples;
import com.ssf.surveillance.repository.IodineRepository;
import com.ssf.surveillance.repository.IodineSamplesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class IodineSamplesService implements IIodineSamplesService {
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    @Autowired
    private IodineSamplesRepository iodineSamplesRepository;

    @Autowired
    private IodineRepository iodineRepository;

    @Autowired
    private AuditorAware<String> auditorProvider;

    @Override
    public SurveillancePageResponse createIodineSamples(String surveillanceId, IodineSamples iodineSamples) {
        iodineSamples.setAudit(getCreatedByAuditor());
        Optional<Iodine> iodineOptional = iodineRepository.findById(surveillanceId);
        if (!iodineOptional.isPresent() && !surveillanceId.equals(iodineSamples.getIodineSurveillanceId())) {
            throw new NoSuchElementException();
        }
        iodineSamples = iodineSamplesRepository.save(iodineSamples);
        log.info("Iodine Samples Created :: {}", iodineSamples.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(iodineSamples)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getIodineSamples(String surveillanceId, String sampleId) {
        List<IodineSamples> results = new ArrayList<>();
        Long totalPages = 0L;
        Long totalElements = 0L;
        Optional<IodineSamples> iodineSamplesOptional = iodineSamplesRepository.findByIdAndIodineSurveillanceId(sampleId, surveillanceId);
        if (iodineSamplesOptional.isPresent()) {
            totalPages = 1L;
            results.add(iodineSamplesOptional.get());
            totalElements = Long.valueOf(results.size());
        }
        return SurveillancePageResponse.builder().totalPages(totalPages).totalElements(totalElements).content(results).build();
    }

    @Override
    public SurveillancePageResponse updateIodineSamples(String surveillanceId, String sampleId, HashMap<String, Object> properties) {
        Optional<IodineSamples> iodineSamplesOptional = iodineSamplesRepository.findByIdAndIodineSurveillanceId(sampleId, surveillanceId);
        IodineSamples iodineSamples;
        Optional<Iodine> iodineOptional = iodineRepository.findById(surveillanceId);
        if (!iodineOptional.isPresent()) {
            throw new NoSuchElementException();
        }
        if (iodineSamplesOptional.isPresent()) {
            iodineSamples = iodineSamplesOptional.get();
        } else {
            throw new NoSuchElementException();
        }
        Map<String, Object> iodineSamplesMap = objectMapper.convertValue(iodineSamples, Map.class);
        iodineSamplesMap.putAll(properties);
        iodineSamples = objectMapper.convertValue(iodineSamplesMap, IodineSamples.class);
        iodineSamples.setAudit(getModifiedByAuditor(iodineSamples.getAudit()));
        iodineSamples = iodineSamplesRepository.save(iodineSamples);
        log.info("Iodine Samples Updated :: {}", iodineSamples.getId());
        return SurveillancePageResponse.builder().totalPages(1L).content(Arrays.asList(iodineSamples)).totalElements(1L).build();
    }

    @Override
    public SurveillancePageResponse getIodineSamplesByFilter(IodineSamplesFilterDTO iodineSamplesFilterDTO) {
        Pageable paging = PageRequest.of(iodineSamplesFilterDTO.getPage(), iodineSamplesFilterDTO.getSize());
        IodineSamples example = new IodineSamples();
        Page<IodineSamples> iodineSamplesPage;
        example.setIodineSurveillanceId(iodineSamplesFilterDTO.getSurveillanceId());
        iodineSamplesPage = iodineSamplesRepository.findAll(Example.of(example), paging);
        return SurveillancePageResponse.builder().totalPages(Long.valueOf(iodineSamplesPage.getTotalPages())).totalElements(iodineSamplesPage.getTotalElements()).content(iodineSamplesPage.getContent()).build();
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

