package com.ssf.childcareservice.controller;

import com.ssf.childcareservice.dtos.ChildCareDto;
import com.ssf.childcareservice.dtos.PageResponse;
import com.ssf.childcareservice.service.ChildCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/childcare")
@Validated
public class ChildCareController  implements  IChildCareController {

    @Autowired
    private ChildCareService service;
    @Override
    public ChildCareDto createChildCare(ChildCareDto request) {
        return service.createChildCare(request);
    }

    @Override
    public ChildCareDto readChildCare(String childCareId) {
        return service.readChildCare(childCareId);
    }

    @Override
    public PageResponse getChildCareByFilter(String citizenId, String childId, Integer page, Integer size) {
        return service.getChildCareByFilter(citizenId, childId, page, size);
    }

    @Override
    public ChildCareDto patchChildCare(String childCareId, Map<String, Object> patchDto) {
        return service.patchChildCare(childCareId, patchDto);
    }
}
