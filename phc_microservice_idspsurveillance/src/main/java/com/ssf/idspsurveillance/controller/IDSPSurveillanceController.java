package com.ssf.idspsurveillance.controller;

import com.ssf.idspsurveillance.dtos.IDSPSurveillanceDto;
import com.ssf.idspsurveillance.dtos.PageResponse;
import com.ssf.idspsurveillance.dtos.PatchDto;
import com.ssf.idspsurveillance.service.IDSPSurveillanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/idspsurveillances")
public class IDSPSurveillanceController implements IIDSPSurveillanceController {

    @Autowired
    private IDSPSurveillanceService service;

    @Override
    public IDSPSurveillanceDto create(IDSPSurveillanceDto request) {
        return service.create(request);
    }

    @Override
    public IDSPSurveillanceDto read(String id) {
        return service.read(id);
    }

    @Override
    public IDSPSurveillanceDto patch(String id, PatchDto patchDto) {
        return service.patch(id, patchDto);
    }

    @Override
    public PageResponse filter(String citizenId, LocalDate stDate, LocalDate edDate, int page, int size) {
        return service.filter(citizenId, stDate, edDate, page, size);
    }
}
