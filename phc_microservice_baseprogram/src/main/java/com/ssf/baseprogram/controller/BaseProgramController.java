package com.ssf.baseprogram.controller;

import com.ssf.baseprogram.dtos.BaseProgramDto;
import com.ssf.baseprogram.dtos.BaseProgramPatchDto;
import com.ssf.baseprogram.dtos.PageResponse;
import com.ssf.baseprogram.service.BaseProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/baseprograms")
public class BaseProgramController implements IBaseProgramController {

	@Autowired
	private BaseProgramService service;

	@Override
	public BaseProgramDto create(BaseProgramDto request) {
		return service.create(request);
	}

	@Override
	public BaseProgramDto read(String id) {
		return service.read(id);
	}

	@Override
    public BaseProgramDto patch(String id, BaseProgramPatchDto patchDto) {
        return service.patch(id,patchDto);
    }

	@Override
    public PageResponse filter(LocalDate stDate, LocalDate edDate, String activityName, String programType, String village , String location, int page, int size) {
        return service.filter(stDate, edDate, activityName, programType, village, location, page, size);
    }
}
