package com.ssf.surveycube.controller;

import com.ssf.surveycube.dtos.FilterDto;
import com.ssf.surveycube.dtos.SurveyCubePageResponse;
import com.ssf.surveycube.entities.SurveyCube;
import com.ssf.surveycube.service.ISurveyCubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/surveycube")
@Validated
public class SurveyCubeController {


    @Autowired
    private ISurveyCubeService service;

    @PostMapping
    public ResponseEntity<SurveyCube> createSurveyCube(@RequestBody SurveyCube cube) throws Exception {
        return ResponseEntity.ok(service.createSurveyCube(cube));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SurveyCube> patchSurveyCube(@PathVariable String id, @RequestBody Map<String, Object> properties) throws Exception {
        return ResponseEntity.ok(service.patchSurveyCube(id, properties));
    }

    @GetMapping("/filter")
    public ResponseEntity<SurveyCubePageResponse> getFilter(@RequestParam MultiValueMap<String, String> filters) {
        return ResponseEntity.ok(service.getSurveyCubeByFilter(filters));
    }


}
