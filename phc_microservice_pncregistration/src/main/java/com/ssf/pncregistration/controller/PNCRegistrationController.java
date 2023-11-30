package com.ssf.pncregistration.controller;

import com.ssf.pncregistration.dtos.PNCRegPageResponse;
import com.ssf.pncregistration.dtos.PNCRegPatchDto;
import com.ssf.pncregistration.dtos.PNCRegistrationDto;
import com.ssf.pncregistration.service.PNCRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pncregistration")
@Validated
public class PNCRegistrationController implements IPNCRegistrationController {

    @Autowired
    private PNCRegistrationService service;

    @Override
    public PNCRegistrationDto registration(PNCRegistrationDto registrationDto) {
        return service.registration(registrationDto);
    }

    @Override
    public PNCRegistrationDto readPNC(String registrationId) {
        return service.readPNC(registrationId);
    }

    @Override
    public PNCRegistrationDto patchPNC(String registrationId, PNCRegPatchDto registrationDto) {
        return service.patchPNC(registrationId,registrationDto);
    }

    @Override
    public PNCRegPageResponse filterPNCReg(String rchId, String regId, String dataEntryStatus, int page, int size) {
        return service.filterPNCReg(rchId,regId,dataEntryStatus,page,size);
    }

}
