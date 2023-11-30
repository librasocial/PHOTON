package com.ssf.pncservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pncservice.dtos.*;
import com.ssf.pncservice.entities.VisitLog;
import com.ssf.pncservice.exception.EntityNotFoundException;
import com.ssf.pncservice.exception.MissmatchException;
import com.ssf.pncservice.repository.IVisitLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class VisitLogService {

    @Autowired
    public AuditorAware<String> auditorProvider;
    @Autowired
    private PNCServiceService pncServiceService;
    @Autowired
    private IVisitLogRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper mapper;


    public VisitLogDto createVisitLog(String serviceId, VisitLogDto visitLogDto) {
        log.debug("Creating the Visitlog for serviceId {}", serviceId);

        //validate
        PNCServiceDto pncServiceDto = mapper.map(pncServiceService.getPNCService(serviceId), PNCServiceDto.class);
        if (!serviceId.equals(visitLogDto.getServiceId())) {
            throw new MissmatchException("serviceId " + serviceId + " missmatch with request service id " + visitLogDto.getServiceId());
        }
        VisitLog visitLog = mapper.map(visitLogDto, VisitLog.class);
        repository.save(visitLog);
        VisitLogDto response = mapper.map(visitLog, VisitLogDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        response.setAudit(auditDto);
        return response;
    }

    public VisitLogDto patchVisitLog(String serviceId, String logId, Map<String, Object> patchDto) {
        log.debug("Update the Visitlog for serviceId {} and logId ", serviceId, logId);
        //validate
        PNCServiceDto pncServiceDto = mapper.map(pncServiceService.getPNCService(serviceId), PNCServiceDto.class);
        var visitLog = getVisitLog(logId, serviceId);
        var visitLogDbMap = objectMapper.convertValue(visitLog, Map.class);
        visitLogDbMap.putAll(patchDto);
        var visitLogEntity = objectMapper.convertValue(visitLogDbMap, VisitLog.class);
        repository.save(visitLogEntity);

        VisitLogDto response = mapper.map(visitLogEntity, VisitLogDto.class);
        // Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        response.setAudit(auditDto);
        return response;

    }

    public VisitLogDto readVisitLog(String serviceId, String logId) {
        log.debug("Read the Visitlog for serviceId {} and logId ", serviceId, logId);
        auditorProvider.getCurrentAuditor();
        VisitLog visitLog = getVisitLog(logId, serviceId);
        VisitLogDto visitLogDto = mapper.map(visitLog, VisitLogDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(visitLog);
        visitLogDto.setAudit(auditDto);

        return visitLogDto;

    }

    public VisitLogDtoPageResponse filterVisitLog(String serviceId, String rchId, int page, int size) {
        log.debug("filter the Visitlog for serviceId {}, rchId {}  ", serviceId, rchId);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        VisitLog example = VisitLog.builder().build();
        if (StringUtils.isNotBlank(serviceId)) {
            example.setServiceId(serviceId);
        }

        if (StringUtils.isNotBlank(rchId)) {
            example.setRchId(rchId);
        }
        return buildVisitLogPageResponse(repository.findAll(Example.of(example), paging));
    }

    public VisitLog getVisitLog(String logId, String serviceId) {
        return repository.findByIdAndServiceId(logId, serviceId)
                .orElseThrow(() -> new EntityNotFoundException("logId :: serviceId " + logId + " :: " + serviceId + " not found"));
    }

    private AuditDto buildAuditDto(VisitLog visitLog) {
        return AuditDto.builder().createdBy(visitLog.getCreatedBy())
                .modifiedBy(visitLog.getModifiedBy())
                .dateCreated(visitLog.getDateCreated())
                .dateModified(visitLog.getDateModified())
                .build();
    }

    private Page<VisitLogDto> buildVisitLogDtos(Page<VisitLog> visitLogs) {
        return visitLogs.map(v -> {
            // Conversion logic
            VisitLogDto visitLogDto = mapper.map(v, VisitLogDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(v);
            visitLogDto.setAudit(auditDto);
            return visitLogDto;
        });
    }

    private VisitLogDtoPageResponse buildVisitLogPageResponse(Page<VisitLog> visitLogs) {
        if (visitLogs == null || visitLogs.isEmpty()) {
            log.debug(" Pagination result is null ");
            return VisitLogDtoPageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + visitLogs.getTotalElements());
            Page<VisitLogDto> visitLogDtos = buildVisitLogDtos(visitLogs);
            return VisitLogDtoPageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages(Long.valueOf(visitLogs.getTotalPages()))
                            .totalElements(Long.valueOf(visitLogs.getTotalElements()))
                            .number(visitLogs.getNumber())
                            .size(visitLogs.getSize())
                            .build())
                    .data(visitLogDtos.toList())
                    .build();
        }
    }
}
