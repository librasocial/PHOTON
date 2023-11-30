package com.ssf.surveillance.controller;

import com.ssf.surveillance.dtos.IodineSamplesFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.IodineSamples;
import com.ssf.surveillance.service.IIodineSamplesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/surveillance/iodines")
@Slf4j
public class IodineSampleController {

    @Autowired
    private IIodineSamplesService service;


    @Operation(summary = "Create Iodine Samples Surveillance", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/{surveillanceId}/samples")
    public ResponseEntity<SurveillancePageResponse> createLarvaSurveillanceVisits(@RequestBody IodineSamples iodineSamples, @PathVariable String surveillanceId) {
        return ResponseEntity.ok(service.createIodineSamples(surveillanceId, iodineSamples));
    }

    @Operation(summary = "Get Iodine Samples Surveillance by id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}/samples/{sampleId}")
    public ResponseEntity<SurveillancePageResponse> getLarvaSurveillanceVisits(@PathVariable String surveillanceId, @PathVariable String sampleId) {
        return ResponseEntity.ok(service.getIodineSamples(surveillanceId, sampleId));
    }


    @Operation(summary = "Update Iodine Samples Surveillance by Id", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{surveillanceId}/samples/{sampleId}")
    public ResponseEntity<SurveillancePageResponse> updateLarvaSurveillanceVisits(@PathVariable String surveillanceId, @PathVariable String sampleId, @RequestBody HashMap<String, Object> patchDTO) {
        return ResponseEntity.ok(service.updateIodineSamples(surveillanceId, sampleId, patchDTO));
    }

    @Operation(summary = "Get Iodine Samples by filter", responses = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{surveillanceId}/samples/filter")
    public ResponseEntity<SurveillancePageResponse> getLarvaSurveillanceVisitsByFilter(@RequestParam(defaultValue = "0") Integer page,
                                                                                       @RequestParam(defaultValue = "5") Integer size,
                                                                                       @PathVariable String surveillanceId) {

        IodineSamplesFilterDTO iodineSamplesFilterDTO = IodineSamplesFilterDTO.builder()
                .page(page).size(size).surveillanceId(surveillanceId).build();
        return ResponseEntity.ok(service.getIodineSamplesByFilter(iodineSamplesFilterDTO));
    }

}
