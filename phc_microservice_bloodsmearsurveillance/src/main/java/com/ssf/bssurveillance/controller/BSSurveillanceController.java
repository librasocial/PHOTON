package com.ssf.bssurveillance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssf.bssurveillance.dtos.BSSurveillanceDto;
import com.ssf.bssurveillance.dtos.PageResponse;
import com.ssf.bssurveillance.dtos.PatchDto;
import com.ssf.bssurveillance.dtos.SurveillanceStatusDto;
import com.ssf.bssurveillance.service.BSSurveillanceService;

@RestController
@RequestMapping("/bloodsmearsurveillances")
public class BSSurveillanceController implements  IBSSurveillanceController {

    @Autowired
    private BSSurveillanceService service;

    @Override
    public BSSurveillanceDto create(BSSurveillanceDto request) {
        return service.create(request);
    }

    @Override
    public BSSurveillanceDto read(String id) {
        return service.read(id);
    }

    @Override
    public BSSurveillanceDto patch(String id, PatchDto patchDto) {
        return service.patch(id, patchDto);
    }

    @Override
    public PageResponse filter(String citizenId, String slideNo, String type, int page, int size) {
        return service.filter(citizenId, slideNo, type, page, size);
    }
    
    @Override
    public List<SurveillanceStatusDto> statusFilter(String citizenIds) {
		return service.statusFilter(List.of(citizenIds.split(",")));
    }
}
