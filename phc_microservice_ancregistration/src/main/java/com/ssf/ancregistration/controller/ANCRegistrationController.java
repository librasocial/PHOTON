package com.ssf.ancregistration.controller;

import com.ssf.ancregistration.dtos.ANCRegPageResponse;
import com.ssf.ancregistration.dtos.ANCRegPatchDto;
import com.ssf.ancregistration.dtos.ANCRegistrationDto;
import com.ssf.ancregistration.service.ANCRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ancregistration")
@Validated
public class ANCRegistrationController implements IANCRegistrationController{

    @Autowired
    private ANCRegistrationService service;

    @Override
    public ANCRegistrationDto registration(ANCRegistrationDto registrationDto) {
        return service.registration(registrationDto);
    }

    @Override
    public ANCRegistrationDto readANC(String registrationId) {
        return service.readANC(registrationId);
    }

    @Override
    public ANCRegistrationDto patchANC(String registrationId, ANCRegPatchDto registrationMap) {
        return service.patchANC(registrationId,registrationMap);
    }

    @Override
    public ANCRegPageResponse filterANCReg(String rchId, String regId, String dataEntryStatus, int page, int size) {
        return service.filterANCReg(rchId,regId,dataEntryStatus,page,size);
    }
}
