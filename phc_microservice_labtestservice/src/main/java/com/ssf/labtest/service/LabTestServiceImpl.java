package com.ssf.labtest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.labtest.dtos.*;
import com.ssf.labtest.entities.LabTest;
import com.ssf.labtest.exception.EntityNotFoundException;
import com.ssf.labtest.repository.ILabTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class LabTestServiceImpl implements ILabTestService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ILabTestRepository labTestRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LabTestDTO createLabTest(LabTestDTO labTestDTO) {
        log.debug("Creating the Lab Test with code {}", labTestDTO.getCode());
        LabTest labTest = mapper.map(labTestDTO, LabTest.class);
        LabTest result = labTestRepository.save(labTest);
        LabTestDTO labTestDTOResult = mapper.map(result, LabTestDTO.class);

        AuditDTO auditDto = buildAuditDto(labTest);
        labTestDTOResult.setAudit(auditDto);

        return labTestDTOResult;
    }

    @Override
    public LabTestDTO getLabTestService(String serviceId) {
        Optional<LabTest> optionalLabTest = labTestRepository.findById(serviceId);
        if (optionalLabTest.isEmpty()) {
            log.debug("No record in DB with the Id {}", serviceId);
            throw new EntityNotFoundException("No record in DB with the Id " + serviceId);
        }
        return mapper.map(optionalLabTest.orElse(null), LabTestDTO.class);
    }

    @Override
    public LabTestDTO editLabTestService(String serviceId, UpdateLabTestDTO updatedLabTestDTO) {
        LabTestDTO labTestDTO = null;
        Optional<LabTest> optionalLabTest = labTestRepository.findById(serviceId);
        if (optionalLabTest.isPresent()) {
            Map<String, Object> dbMap = objectMapper.convertValue(optionalLabTest.get(), Map.class);
            Map<String, Object> mapWithUpdatedValues = objectMapper.convertValue(updatedLabTestDTO, Map.class);
            dbMap.putAll(mapWithUpdatedValues);
            LabTest labTest = objectMapper.convertValue(dbMap, LabTest.class);
            labTestRepository.save(labTest);
            labTestDTO = mapper.map(labTest, LabTestDTO.class);

            AuditDTO auditDto = buildAuditDto(optionalLabTest.get());
            labTestDTO.setAudit(auditDto);
        } else {
            log.debug("No record in DB with the Id {}", serviceId);
            throw new EntityNotFoundException("No record in DB with the Id " + serviceId);
        }
        return labTestDTO;
    }

    @Override
    public LabTestPageResponse retrieveLabTestServices(String labTestIds, String search, int page, int size) {
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        Page<LabTest> labTests;
        if (StringUtils.isNotEmpty(labTestIds)) {
            List<String> labIds = List.of(labTestIds.split(","));
            labTests = labTestRepository.findAllByIdContaining(labIds, paging);
        } else if (StringUtils.isNotEmpty(search)) {
            labTests = labTestRepository.searchByName(search, paging);
        } else {
            labTests = labTestRepository.findAll(paging);
        }
        return buildLabTestPageResponse(labTests);
    }

    private LabTestPageResponse buildLabTestPageResponse(Page<LabTest> labTests) {
        if (labTests == null || labTests.isEmpty()) {
            return LabTestPageResponse.builder().meta(PageDTO.builder().totalPages(Long.valueOf(labTests.getTotalPages()))
                            .totalElements(Long.valueOf(labTests.getTotalElements()))
                            .number(labTests.getNumber()).size(labTests.getSize()).build())
                    .data(new ArrayList<>()).build();
        } else {
            return LabTestPageResponse.builder().meta(PageDTO.builder().totalPages(Long.valueOf(labTests.getTotalPages()))
                            .totalElements(Long.valueOf(labTests.getTotalElements()))
                            .number(labTests.getNumber()).size(labTests.getSize()).build())
                    .data(buildLabTestDTOs(labTests).toList()).build();
        }
    }

    private Page<LabTestDTO> buildLabTestDTOs(Page<LabTest> labTests) {
        return labTests.map(labTest -> {
            LabTestDTO labTestDTO = mapper.map(labTest, LabTestDTO.class);
            AuditDTO auditDTO = buildAuditDto(labTest);
            labTestDTO.setAudit(auditDTO);
            return labTestDTO;
        });
    }

    private AuditDTO buildAuditDto(LabTest labTest) {
        return AuditDTO.builder().createdBy(labTest.getCreatedBy())
                .modifiedBy(labTest.getModifiedBy())
                .dateCreated(labTest.getDateCreated())
                .dateModified(labTest.getDateModified())
                .build();
    }
}
