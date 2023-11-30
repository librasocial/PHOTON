package com.ssf.eligiblecouple.controller;

import com.ssf.eligiblecouple.dtos.EligibleCoupleDto;
import com.ssf.eligiblecouple.dtos.EligibleCouplePageResponse;
import com.ssf.eligiblecouple.service.EligibleCoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ecservices")
@Validated
public class EligibleCoupleController implements  IEligibleCoupleController {
    @Autowired
    private EligibleCoupleService service;

    @Override
    public EligibleCoupleDto createEc(EligibleCoupleDto eligibleCoupleDto) {
        return service.createEc(eligibleCoupleDto);
    }

    @Override
    public EligibleCoupleDto readEc(String serviceId) {
        return service.readEc(serviceId);
    }

    @Override
    public EligibleCoupleDto patchEc(String serviceId, Map<String, Object> eligibleCoupleDto) {
        return service.patchEc(serviceId, eligibleCoupleDto);
    }

    @Override
    public EligibleCouplePageResponse filterEc(String rchId, String serviceId, String dataEntryStatus, int page, int size) {
        return service.filterEc(rchId, serviceId, dataEntryStatus, page, size);
    }
}
