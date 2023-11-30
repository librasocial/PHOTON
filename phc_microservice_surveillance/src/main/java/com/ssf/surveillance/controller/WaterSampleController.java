package com.ssf.surveillance.controller;

import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.dtos.WaterSamplesFilterDTO;
import com.ssf.surveillance.entities.WaterSamples;
import com.ssf.surveillance.service.IWaterSamplesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/surveillance/watersamples")
@Slf4j
public class WaterSampleController {

    @Autowired
    private IWaterSamplesService service;


    @Operation(summary = "Create WaterSamples Surveillance", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("")
    public ResponseEntity<SurveillancePageResponse> createWaterSamplesSurveillance(@RequestBody WaterSamples waterSamples) {
        return ResponseEntity.ok(service.createWaterSamplesSurveillance(waterSamples));
    }

    @Operation(summary = "Get WaterSamples Surveillance by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}")
    public ResponseEntity<SurveillancePageResponse> getWaterSamplesSurveillance(@PathVariable String surveillanceId) {
        return ResponseEntity.ok(service.getWaterSamplesSurveillance(surveillanceId));
    }


    @Operation(summary = "Update WaterSamples Surveillance by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{surveillanceId}")
    public ResponseEntity<SurveillancePageResponse> updateWaterSamplesSurveillance(@PathVariable String surveillanceId, @RequestBody HashMap<String, Object> patchDTO) {
        return ResponseEntity.ok(service.updateWaterSamplesSurveillance(surveillanceId, patchDTO));
    }

    @Operation(summary = "Get WaterSamples by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/filter")
    public ResponseEntity<SurveillancePageResponse> getWaterSamplesSurveillanceByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                                        @RequestParam(defaultValue = "5") Integer size,
                                                                                        @RequestParam Optional<String> dateOfSurvey,
                                                                                        @RequestParam Optional<String> placeType,
                                                                                        @RequestParam Optional<String> villageId,
                                                                                        @RequestParam Optional<String> placeOrgId,
                                                                                        @RequestParam Optional<String> householdId) {

        WaterSamplesFilterDTO waterSamplesFilterDTO = WaterSamplesFilterDTO.builder()
                .page(page).size(size)
                .dateOfSurvey(dateOfSurvey).householdId(householdId).placeOrgId(placeOrgId)
                .placeType(placeType).villageId(villageId)
                .build();
        return ResponseEntity.ok(service.getWaterSamplesSurveillanceByFilter(waterSamplesFilterDTO));
    }


}
