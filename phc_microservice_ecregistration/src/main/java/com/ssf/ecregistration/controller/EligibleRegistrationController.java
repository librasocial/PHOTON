package com.ssf.ecregistration.controller;

import com.ssf.ecregistration.dtos.EligibleRegPageResponse;
import com.ssf.ecregistration.dtos.EligibleRegPatchDto;
import com.ssf.ecregistration.dtos.EligibleRegistrationDto;
import com.ssf.ecregistration.service.EligibleRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ecregistrations")
@Validated
public class EligibleRegistrationController implements IEligibleRegistrationController {

    @Autowired
    private EligibleRegistrationService service;

    @Override
    public EligibleRegistrationDto registration(EligibleRegistrationDto registrationDto) {
        return service.registration(registrationDto);
    }

    @Override
    public EligibleRegistrationDto registrationPatch(String registrationId, EligibleRegPatchDto request) {
        return service.registrationPatch(registrationId, request);
    }

    @Override
    public EligibleRegistrationDto getECRegistrationById(String registrationId) {
        return service.getECRegistrationById(registrationId);
    }

    @Override
    public EligibleRegPageResponse filterEcReg(String rchId, String regId, String dataEntryStatus, int page, int size) {
        return service.filterEcReg(rchId, regId, dataEntryStatus, page, size);
    }
}
