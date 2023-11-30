package com.ssf.pncservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncservice.dtos.*;
import com.ssf.pncservice.entities.InfantVisitLog;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.repository.IInfantVisitlogRepository;
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
public class InfantVisitLogService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private PNCServiceService pncServiceService;
    @Autowired
    private IInfantVisitlogRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;
    
    


    public InfantVisitLogDto createInfantVisitLog(String childId, InfantVisitLogDto infantVisitLog) {
        log.debug("Creating the InfantVisitLog for childId {}", childId);

        //validate
        pncServiceService.getPNCService(infantVisitLog.getServiceId());

        InfantVisitLog entity = mapper.map(infantVisitLog, InfantVisitLog.class);
        repository.save(entity);
        InfantVisitLogDto response = mapper.map(entity, InfantVisitLogDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(entity);
        response.setAudit(auditDto);
        return response;
    }

    public InfantVisitLogDto patchInfantVisitLog(String childId, String logId, Map<String, Object> patchDto) {
        log.debug("Update the InfantVisitLog for childId {} and logId ", childId, logId);

        var existingEntity = getInfantVisitLog(logId, childId);
        var visitLogDbMap = objectMapper.convertValue(existingEntity, Map.class);
        visitLogDbMap.putAll(patchDto);
        var entity = objectMapper.convertValue(visitLogDbMap, InfantVisitLog.class);
        repository.save(entity);

        InfantVisitLogDto response = mapper.map(entity, InfantVisitLogDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(entity);
        response.setAudit(auditDto);
        return response;

    }

    public InfantVisitLogDto readInfantVisitLog(String childId, String logId) {
        log.debug("Read the InfantVisitLog for childId {} and logId ", childId, logId);
        auditorProvider.getCurrentAuditor();
        var entity = getInfantVisitLog(logId, childId);
        var dto = mapper.map(entity, InfantVisitLogDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(entity);
        dto.setAudit(auditDto);

        return dto;

    }

    public InfantVisitLogPageResponse filterInfantVisitLog(String serviceId, String childId, String logId, int page, int size) {
        log.debug("filter the InfantVisitLog for serviceId {}, rchId {} , logId {}  ", serviceId, childId, logId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        InfantVisitLog example = InfantVisitLog.builder().build();
        if (StringUtils.isNotBlank(serviceId)) {
            example.setServiceId(serviceId);
        }

        if (StringUtils.isNotBlank(childId)) {
            example.setChildId(childId);
        }

        if (StringUtils.isNotBlank(logId)) {
            example.setId(logId);
        }

        return buildVisitLogPageResponse(repository.findAll(Example.of(example), paging));
    }

    public InfantVisitLog getInfantVisitLog(String logId, String childId) {
        return repository.findByIdAndChildId(logId, childId)
                .orElseThrow(() -> new EntityNotFoundException("logId :: childId " + logId + " :: " + childId + " not found"));
    }

    private AuditDto buildAuditDto(InfantVisitLog infantVisitLog) {
        return AuditDto.builder().createdBy(infantVisitLog.getCreatedBy())
                .modifiedBy(infantVisitLog.getModifiedBy())
                .dateCreated(infantVisitLog.getDateCreated())
                .dateModified(infantVisitLog.getDateModified())
                .build();
    }

    private Page<InfantVisitLogDto> buildInfantVisitLogs(Page<InfantVisitLog> infantVisitLog) {
        return infantVisitLog.map(v -> {
            // Conversion logic
            InfantVisitLogDto dto = mapper.map(v, InfantVisitLogDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(v);
            dto.setAudit(auditDto);
            return dto;
        });
    }

    private InfantVisitLogPageResponse buildVisitLogPageResponse(Page<InfantVisitLog> visitLogs) {
        if (visitLogs == null || visitLogs.isEmpty()) {
            log.debug(" Pagination result is null ");
            return InfantVisitLogPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + visitLogs.getTotalElements());
            Page<InfantVisitLogDto> dtos = buildInfantVisitLogs(visitLogs);
            return InfantVisitLogPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages(Long.valueOf(visitLogs.getTotalPages()))
                            .totalElements(Long.valueOf(visitLogs.getTotalElements()))
                            .number(visitLogs.getNumber())
                            .size(visitLogs.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }
}
