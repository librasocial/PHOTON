package com.ssf.labtest.controller;

import com.ssf.labtest.constants.ValidatorConstants;
import com.ssf.labtest.dtos.LabTestDTO;
import com.ssf.labtest.dtos.LabTestPageResponse;
import com.ssf.labtest.dtos.UpdateLabTestDTO;
import com.ssf.labtest.exception.LabTestException;
import com.ssf.labtest.service.ILabTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/labtestservices")
public class LabTestController {

    @Autowired
    private ILabTestService labTestService;

    @Operation(summary = "Creates a Lab Test", responses = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping
    public ResponseEntity<LabTestDTO> createLabTest(@Valid @RequestBody LabTestDTO labTestDTO) throws Exception {
        validateTurnAroundUnit(labTestDTO);
        validateOutSourcedOrganization(labTestDTO);

        return new ResponseEntity<LabTestDTO>(labTestService.createLabTest(labTestDTO), HttpStatus.CREATED);
    }

    private void validateOutSourcedOrganization(LabTestDTO labTestDTO) throws LabTestException {
        if (labTestDTO.getOutSourced() && labTestDTO.getOutsourcedOrg() == null) {
            throw new LabTestException("outsourcedOrg cannot be empty when outSourced is set to true.");
        }
    }

    private void validateTurnAroundUnit(LabTestDTO labTestDTO) throws LabTestException {
        if (StringUtils.isNotEmpty(labTestDTO.getTurnAroundTime())
                && StringUtils.isEmpty(labTestDTO.getTurnAroundUnit())) {
            throw new LabTestException(ValidatorConstants.TURN_AROUND_UNIT_EMPTY);
        }
    }

    @Operation(summary = "Retrieves a LabTestService", responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping("/{serviceId}")
    public ResponseEntity<LabTestDTO> getLabTestService(@PathVariable("serviceId") String serviceId) {
        return new ResponseEntity<LabTestDTO>(labTestService.getLabTestService(serviceId), HttpStatus.OK);
    }

    @Operation(summary = "Updates or Sets a LabTestService's properties", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @PatchMapping("/{serviceId}")
    public ResponseEntity<LabTestDTO> editLabTestService(@PathVariable("serviceId") String serviceId,
                                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                                                                 schema = @Schema(implementation = LabTestDTO.class))) @Valid @RequestBody UpdateLabTestDTO labTestDTO) {
        return new ResponseEntity<LabTestDTO>(labTestService.editLabTestService(serviceId, labTestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieves LabTestService based on filters", responses = {
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "The specified resource was not found")
    })
    @GetMapping(value = "/filter")
    public LabTestPageResponse retrieveLabTestServices(@RequestParam(value = "labTestIds", required = false) String labTestIds,
                                                       @RequestParam(value = "search", required = false) String search,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return labTestService.retrieveLabTestServices(labTestIds, search, page, size);
    }

}
