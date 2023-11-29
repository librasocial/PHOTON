package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.InfantVisitLogDto;
import com.ssf.pncservice.dtos.InfantVisitLogPageResponse;
import com.ssf.pncservice.service.InfantVisitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pncservices")
@Validated
public class InfantVisitLogController implements  IInfantVisitLogController {

    @Autowired
    private InfantVisitLogService service;
    @Override
    public InfantVisitLogDto createInfantVisitLog(String childId, InfantVisitLogDto request) {
        return service.createInfantVisitLog(childId,request);
    }

    @Override
    public InfantVisitLogDto patchInfantVisitLog(String childId, String logId, Map<String, Object> request) {
        return service.patchInfantVisitLog(childId,logId, request);
    }

    @Override
    public InfantVisitLogDto readInfantVisitLog(String childId, String logId) {
        return service.readInfantVisitLog(childId,logId);
    }

    @Override
    public InfantVisitLogPageResponse filterInfantVisitLog(String childId, String serviceId, String logId, int page, int size) {
        return service.filterInfantVisitLog(serviceId, childId, logId, page, size);
    }
}
