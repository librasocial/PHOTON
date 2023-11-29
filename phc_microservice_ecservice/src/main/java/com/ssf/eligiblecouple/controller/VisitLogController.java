package com.ssf.eligiblecouple.controller;

import com.ssf.eligiblecouple.dtos.VisitLogDto;
import com.ssf.eligiblecouple.dtos.VisitLogDtoPageResponse;
import com.ssf.eligiblecouple.service.VisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ecservices")
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
    public VisitLogDtoPageResponse filterVisitLog(String serviceId, String logId, String rchId, int page, int size) {
        return service.filterVisitLog(serviceId, logId, rchId, page, size);
    }
}
