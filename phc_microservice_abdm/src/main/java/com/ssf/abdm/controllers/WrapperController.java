package com.ssf.abdm.controllers;

import com.ssf.abdm.dto.RequestDTO;
import com.ssf.abdm.services.IWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WrapperController {

    @Autowired
    private IWrapperService service;

    @PostMapping(value = "/v0.5/users/auth/fetch-modes")
    public ResponseEntity<RequestDTO> initilizeFetchModes(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.initilizeUserAuthFetchMode(data));
    }

    @PostMapping(value = "/v0.5/users/auth/init")
    public ResponseEntity<RequestDTO> initilizeInItAuth(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.initilizeUserAuthInIt(data));
    }

    @PostMapping(value = "/v0.5/users/auth/confirm")
    public ResponseEntity<RequestDTO> initilizeConfirmAuth(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.initilizeUserAuthConfirm(data));
    }

    @PostMapping(value = "/devservice/v1/bridges/addUpdateServices")
    public ResponseEntity<?> addUpdateService(@RequestBody List<Map<String, Object>> data) {
        return ResponseEntity.ok(service.addUpdateService(data));
    }

    @GetMapping(value = "/devservice/v1/bridges/getServices")
    public ResponseEntity<?> getService() {
        return ResponseEntity.ok(service.getService());
    }

    @PostMapping(value = "/devservice/v1/bridges")
    public ResponseEntity<?> updateBridge(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.updateBridge(data));
    }


    @PostMapping(value = "/esanjeevani/api/v1/Patient")
    public ResponseEntity<?> createESanjeevani(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.createESanjeevani(data));
    }

    @PostMapping(value = "/esanjeevani/api/ThirdPartyAuth/authenticateReference")
    public ResponseEntity<?> createESanjeevaniSSOToken(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.createESanjeevaniSSOToken(data));
    }

    @PostMapping(value = "/eAushadha/api/DWInstituteInward")
    public ResponseEntity<?> getEAushadhaInward(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.getEAushadhaInward(data));
    }

    @PostMapping(value = "/eAushadha/api/DWInstituteoutward")
    public ResponseEntity<?> getEAushadhaOutward(@RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(service.getEAushadhaOutward(data));
    }

}
