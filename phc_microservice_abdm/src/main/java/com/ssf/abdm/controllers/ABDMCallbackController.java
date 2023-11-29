package com.ssf.abdm.controllers;


import com.ssf.abdm.constant.RequestType;
import com.ssf.abdm.entities.Response;
import com.ssf.abdm.services.IABDMService;
import com.ssf.abdm.services.ISendResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/callback")
public class ABDMCallbackController {

    @Autowired
    private IABDMService service;

    @Autowired
    private ISendResponseService sendResponseService;

    @GetMapping("/response/{requestId}")
    public ResponseEntity<Response> getCallbackResponse(@PathVariable String requestId) {
        return ResponseEntity.ok(service.getResponse(requestId));
    }

    @PostMapping("/v0.5/users/auth/on-fetch-modes")
    public ResponseEntity<Void> userAuthFetchMode(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        service.persistResponse(data, RequestType.USER_AUTH_FETCH_MODE);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/v0.5/users/auth/on-init")
    public ResponseEntity<Void> userAuthInIt(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        service.persistResponse(data, RequestType.USER_AUTH_INIT);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/v0.5/users/auth/on-confirm")
    public ResponseEntity<Void> userAuthConfirm(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        service.persistResponse(data, RequestType.USER_AUTH_CONFIRM);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("v1.0/patients/profile/share")
    public ResponseEntity<Object> profileShare(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        sendResponseService.sendShareProfileResponse(data);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/v0.5/care-contexts/discover")
    public ResponseEntity<Object> careContextDiscover(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        sendResponseService.sendCareContextDiscoverResponse(data);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/v0.5/links/link/init")
    public ResponseEntity<Object> linkInit(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        sendResponseService.sendLinkInItResponse(data);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/v0.5/links/link/confirm")
    public ResponseEntity<Object> linkConfirm(@RequestBody Map<String, Object> data) {
        log.debug(String.valueOf(data));
        sendResponseService.sendLinkConfirmResponse(data);
        return ResponseEntity.accepted().build();
    }

}


