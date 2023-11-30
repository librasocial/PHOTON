package com.ssf.immunizationrec.controller;

import com.ssf.immunizationrec.dtos.FilterDto;
import com.ssf.immunizationrec.dtos.ImmunizationRecDto;
import com.ssf.immunizationrec.dtos.PageResponse;
import com.ssf.immunizationrec.service.ImmunizationRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/immunizationRecommendations")
public class ImmunizationRecController implements IImmunizationRecController {

    @Autowired
    private ImmunizationRecService service;

    @Override
    public ImmunizationRecDto createImmunRec(ImmunizationRecDto request) {
        return service.createImmunRec(request);
    }

    @Override
    public ImmunizationRecDto readImmunRec(String id) {
        return service.readImmunRec(id);
    }

    @Override
    public PageResponse getImmunRecByFilter(FilterDto filterDto) {
        return service.getImmunRecByFilter(filterDto);
    }

    @Override
    public ImmunizationRecDto patchImmunRec(String id, Map<String, Object> request) {
        return service.patchImmunRec(id,request);
    }
}
