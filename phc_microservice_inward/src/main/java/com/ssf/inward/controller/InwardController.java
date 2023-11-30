package com.ssf.inward.controller;

import com.ssf.inward.dtos.InwardDto;
import com.ssf.inward.dtos.InwardPatchDto;
import com.ssf.inward.dtos.PageResponse;
import com.ssf.inward.service.InwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/inwards")
public class InwardController implements IInwardController{

    @Autowired
    private InwardService service;
    @Override
    public InwardDto createInward(InwardDto request) {
        return service.createInward(request);
    }

    @Override
    public InwardDto readInward(String id) {
        return service.readInward(id);
    }

    @Override
    public InwardDto patchInward(String id, InwardPatchDto patchDto) {
        return service.patchInward(id, patchDto);
    }

    @Override
    public PageResponse filterInward(LocalDate stDate, LocalDate edDate, String inwardType, String supplier, String status, int page, int size) {
        return service.filterInward(stDate, edDate, inwardType, supplier, status, page, size);
    }
}
