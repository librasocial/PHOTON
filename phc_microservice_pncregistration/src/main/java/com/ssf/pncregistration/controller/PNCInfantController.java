package com.ssf.pncregistration.controller;

import com.ssf.pncregistration.dtos.InfantDto;
import com.ssf.pncregistration.dtos.InfantPageResponse;
import com.ssf.pncregistration.dtos.InfantPatchDto;
import com.ssf.pncregistration.service.PNCInfantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/pncregistration")
@Validated
public class PNCInfantController implements IPNCInfantController {

    @Autowired
    private PNCInfantService service;

    @Override
    public List<InfantDto> createInfant(String registrationId, Map<String,List<InfantDto>> infantDto) {
    	List<InfantDto> infantList = infantDto.get("infantregistration");
        return service.createInfant(registrationId, infantList);
    }

    @Override
    public InfantDto readInfants(String registrationId, String infantId) {
        return service.readInfants(registrationId, infantId);
    }

    @Override
    public InfantDto patchInfant(String registrationId, String infantId, InfantPatchDto infantPatchDto) {
        return service.patchInfant(registrationId, infantId, infantPatchDto);
    }

    @Override
    public InfantPageResponse filterInfants(String infantId, String regId, int page, int size) {
        return service.filterInfants(infantId,regId,page,size);
    }
}
