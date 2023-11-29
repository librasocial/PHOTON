package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.VisitLogDto;
import com.ssf.pncservice.dtos.VisitLogDtoPageResponse;
import com.ssf.pncservice.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pncservices")
@Validated
public class VisitLogController implements IVisitLogController {

    @Autowired
    private VisitLogService service;

    @Override
    public VisitLogDto createVisitLog(String serviceId, VisitLogDto request) {
        return service.createVisitLog(serviceId, request);
    }

    @Override
    public VisitLogDto patchVisitLog(String serviceId, String logId, Map<String, Object> request) {
        return service.patchVisitLog(serviceId, logId, request);
    }

    @Override
    public VisitLogDto readVisitLog(String serviceId, String logId) {
        return service.readVisitLog(serviceId, logId);
    }

    @Override
    public VisitLogDtoPageResponse filterVisitLog(String serviceId, String rchId, int page, int size) {
        return service.filterVisitLog(serviceId, rchId, page, size);
    }
}
