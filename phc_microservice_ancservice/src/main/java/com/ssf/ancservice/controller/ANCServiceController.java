package com.ssf.ancservice.controller;

import com.ssf.ancservice.constant.DataEntryStatusEnum;
import com.ssf.ancservice.dtos.ANCPageResponse;
import com.ssf.ancservice.dtos.ANCServiceDto;
import com.ssf.ancservice.dtos.ANCServicePatchDto;
import com.ssf.ancservice.entities.ANCService;
import com.ssf.ancservice.service.ANCServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/ancservices")
@Validated
public class ANCServiceController implements IANCServiceController {

    @Autowired
    private ANCServiceService service;

    @Override
    public ANCServiceDto createANC(ANCServiceDto registrationDto) {
        return service.createANC(registrationDto);
    }

    @Override
    public ANCServiceDto patchANC(String serviceId, ANCServicePatchDto patchDto) {
        return service.patchANC(serviceId, patchDto);
    }

    @Override
    public ANCService getANC(String serviceId) {
        return service.getANCService(serviceId);
    }

    @Override
    public ANCPageResponse getANCByFilter(Optional<String> rchId, Optional<String> serviceId, Optional<DataEntryStatusEnum> dataEntryStatus, Integer page, Integer size) {
        return service.getANCByFilter(rchId, serviceId, dataEntryStatus, page, size);
    }

}
