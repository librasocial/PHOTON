package com.ssf.pharmacyorder.controller;

import com.ssf.pharmacyorder.dtos.DispenseDto;
import com.ssf.pharmacyorder.dtos.PageResponse;
import com.ssf.pharmacyorder.service.PharmacyDispenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/pharmacyorders")
public class PharmacyDispenseController implements IPharmacyDispenseController {
    @Autowired
    private PharmacyDispenseService service;

    @Override
    public DispenseDto createDispense(String orderId, DispenseDto request) {
        return service.createDispense(orderId, request);
    }

    @Override
    public DispenseDto readDispense(String orderId, String dispenseId) {
        return service.readDispense(orderId, dispenseId);
    }

    @Override
    public DispenseDto pharmacyDispensePatch(String orderId, String dispenseId, Map<String, Object> patchDto) {
        return service.pharmacyDispensePatch(orderId, dispenseId, patchDto);
    }

    @Override
    public PageResponse filterPharmacyDispense(LocalDate stDate, LocalDate edDate, String orderId, int page, int size) {
        return service.filterPharmacyDispense(stDate, edDate, orderId, page, size);
    }
}
