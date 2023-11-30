package com.ssf.pncservice.controller;

import com.ssf.pncservice.dtos.PNCPageResponse;
import com.ssf.pncservice.dtos.PNCServiceDto;
import com.ssf.pncservice.dtos.PNCServicePatchDto;
import com.ssf.pncservice.service.PNCServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pncservices")
@Validated
public class PNCServiceController implements IPNCServiceController {

    @Autowired
    private PNCServiceService service;
    @Override
    public PNCServiceDto createPNC(PNCServiceDto pncServiceDto) {
        return service.createPNC(pncServiceDto);
    }

    @Override
    public PNCServiceDto patchPNC(String serviceId, PNCServicePatchDto patchDto) {
        return service.patchPNC(serviceId,patchDto);
    }

    @Override
    public PNCServiceDto getPNC(String serviceId) {
        return service.getPNC(serviceId);
    }

    @Override
    public PNCPageResponse getPNCByFilter(String rchId, String serviceId, Integer page, Integer size) {
        return service.getPNCByFilter(rchId, serviceId, page, size);
    }
}
