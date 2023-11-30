package com.ssf.surveillance.controller;

import com.ssf.surveillance.dtos.LarvaVisitsFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.LarvaVisits;
import com.ssf.surveillance.service.ILarvaVisitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/surveillance/larvas")
@Slf4j
public class LarvaVisitsController {

    @Autowired
    private ILarvaVisitsService service;


    @Operation(summary = "Create Larva Visits Surveillance", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{surveillanceId}/visits")
    public ResponseEntity<SurveillancePageResponse> createLarvaSurveillanceVisits(@RequestBody LarvaVisits larvaVisits, @PathVariable String surveillanceId) {
        return ResponseEntity.ok(service.createLarvaSurveillanceVisits(surveillanceId, larvaVisits));
    }

    @Operation(summary = "Get Larva Visits Surveillance by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}/visits/{visitId}")
    public ResponseEntity<SurveillancePageResponse> getLarvaSurveillanceVisits(@PathVariable String surveillanceId, @PathVariable String visitId) {
        return ResponseEntity.ok(service.getLarvaSurveillanceVisits(surveillanceId, visitId));
    }


    @Operation(summary = "Update Larva Visits Surveillance by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{surveillanceId}/visits/{visitId}")
    public ResponseEntity<SurveillancePageResponse> updateLarvaSurveillanceVisits(@PathVariable String surveillanceId, @PathVariable String visitId, @RequestBody HashMap<String, Object> patchDTO) {
        return ResponseEntity.ok(service.updateLarvaSurveillanceVisits(surveillanceId, visitId, patchDTO));
    }

    @Operation(summary = "Get Larva Visits by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}/visits/filter")
    public ResponseEntity<SurveillancePageResponse> getLarvaSurveillanceVisitsByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                                       @RequestParam(defaultValue = "5") Integer size,
                                                                                       @PathVariable String surveillanceId) {

        LarvaVisitsFilterDTO larvaVisitsFilterDTO = LarvaVisitsFilterDTO.builder()
                .page(page).size(size).surveillanceId(surveillanceId).build();
        return ResponseEntity.ok(service.getLarvaSurveillanceVisitsByFilter(larvaVisitsFilterDTO));
    }

}
