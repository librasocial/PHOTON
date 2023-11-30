package com.ssf.surveillance.controller;

import com.ssf.surveillance.dtos.LarvaFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Larva;
import com.ssf.surveillance.service.ILarvaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/surveillance/larvas")
@Slf4j
public class LarvaController {

    @Autowired
    private ILarvaService service;


    @Operation(summary = "Create Larva Surveillance", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("")
    public ResponseEntity<SurveillancePageResponse> createLarvaSurveillance(@RequestBody Larva larva) {
        return ResponseEntity.ok(service.createLarvaSurveillance(larva));
    }

    @Operation(summary = "Get Larva Surveillance by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}")
    public ResponseEntity<SurveillancePageResponse> getLarvaSurveillance(@PathVariable String surveillanceId) {
        return ResponseEntity.ok(service.getLarvaSurveillance(surveillanceId));
    }


    @Operation(summary = "Update Larva Surveillance by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{surveillanceId}")
    public ResponseEntity<SurveillancePageResponse> updateLarvaSurveillance(@PathVariable String surveillanceId, @RequestBody HashMap<String, Object> patchDTO) {
        return ResponseEntity.ok(service.updateLarvaSurveillance(surveillanceId, patchDTO));
    }

    @Operation(summary = "Get Larva by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/filter")
    public ResponseEntity<SurveillancePageResponse> getLarvaSurveillanceByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                         @RequestParam(defaultValue = "5") Integer size,
                                                                         @RequestParam Optional<String> dateOfSurvey,
                                                                         @RequestParam Optional<String> placeType,
                                                                         @RequestParam Optional<String> villageId,
                                                                         @RequestParam Optional<String> placeOrgId,
                                                                         @RequestParam Optional<String> householdId) {

        LarvaFilterDTO larvaFilterDTO = LarvaFilterDTO.builder()
                .page(page).size(size)
                .dateOfSurvey(dateOfSurvey).householdId(householdId).placeOrgId(placeOrgId)
                .placeType(placeType).villageId(villageId)
                .build();
        return ResponseEntity.ok(service.getLarvaSurveillanceByFilter(larvaFilterDTO));
    }


}
