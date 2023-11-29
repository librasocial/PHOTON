package com.ssf.immunization.controller;


import com.ssf.immunization.dtos.FilterDto;
import com.ssf.immunization.dtos.ImmunizationDto;
import com.ssf.immunization.dtos.PageResponse;
import com.ssf.immunization.service.ImmunizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/immunizations")
public class ImmunizationController implements IImmunizationController {

    @Autowired
    private ImmunizationService service;

    @Override
    public ImmunizationDto createImmun(ImmunizationDto request) {
        return service.createImmun(request);
    }

    @Override
    public ImmunizationDto readImmun(String id) {
        return service.readImmun(id);
    }

    @Override
    public PageResponse getImmunByFilter(FilterDto filterDto) {
        return service.getImmunByFilter(filterDto);
    }

    @Override
    public ImmunizationDto patchImmun(String id, Map<String, Object> request) {
        return service.patchImmun(id,request);
    }
}
