package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.InfantDto;
import com.ssf.pncservice.dtos.InfantPageResponse;
import com.ssf.pncservice.service.InfantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pncservices")
@Validated
public class InfantController implements IInfantController {

    @Autowired
    private InfantService service;
    @Override
    public InfantDto createInfant(String serviceId, InfantDto infantDto) {
        return service.createInfant(serviceId, infantDto);
    }

    @Override
    public InfantDto patchInfant(String serviceId, String infantId, Map<String, Object> patchDto) {
        return service.patchInfant(serviceId,infantId,patchDto);
    }

    @Override
    public InfantDto getInfant(String serviceId, String infantId) {
        return service.getInfant(serviceId, infantId);
    }

    @Override
    public InfantPageResponse getInfantByFilter(String childId, String serviceId, String infantId, Integer page, Integer size) {
        return service.getInfantByFilter(childId, serviceId, infantId, page, size);
    }
}
