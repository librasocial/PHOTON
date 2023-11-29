package com.ssf.surveillance.controller;

import com.ssf.surveillance.dtos.IodineFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Iodine;
import com.ssf.surveillance.service.IIodineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/surveillance/iodines")
@Slf4j
public class IodineController {

    @Autowired
    private IIodineService service;


    @Operation(summary = "Create Iodine Surveillance", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("")
    public ResponseEntity<SurveillancePageResponse> createIodineSurveillance(@RequestBody Iodine iodine) {
        return ResponseEntity.ok(service.createIodineSurveillance(iodine));
    }

    @Operation(summary = "Get Iodine Surveillance by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}")
    public ResponseEntity<SurveillancePageResponse> getIodineSurveillance(@PathVariable String surveillanceId) {
        return ResponseEntity.ok(service.getIodineSurveillance(surveillanceId));
    }


    @Operation(summary = "Update Iodine Surveillance by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{surveillanceId}")
    public ResponseEntity<SurveillancePageResponse> updateIodineSurveillance(@PathVariable String surveillanceId, @RequestBody HashMap<String, Object> patchDTO) {
        return ResponseEntity.ok(service.updateIodineSurveillance(surveillanceId, patchDTO));
    }

    @Operation(summary = "Get Iodine by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/filter")
    public ResponseEntity<SurveillancePageResponse> getIodineSurveillanceByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                                  @RequestParam(defaultValue = "5") Integer size,
                                                                                  @RequestParam Optional<String> dateOfSurvey,
                                                                                  @RequestParam Optional<String> placeType,
                                                                                  @RequestParam Optional<String> villageId,
                                                                                  @RequestParam Optional<String> placeOrgId,
                                                                                  @RequestParam Optional<String> householdId) {

        IodineFilterDTO iodineFilterDTO = IodineFilterDTO.builder()
                .page(page).size(size)
                .dateOfSurvey(dateOfSurvey).householdId(householdId).placeOrgId(placeOrgId)
                .placeType(placeType).villageId(villageId)
                .build();
        return ResponseEntity.ok(service.getIodineSurveillanceByFilter(iodineFilterDTO));
    }


}
