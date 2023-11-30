package com.ssf.pharmacyorder.controller;

import com.ssf.pharmacyorder.dtos.PageResponse;
import com.ssf.pharmacyorder.dtos.PharmacyOrderDto;
import com.ssf.pharmacyorder.dtos.PharmacyOrderPatchDto;
import com.ssf.pharmacyorder.service.PharmacyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/pharmacyorders")
public class PharmacyOrderController implements IPharmacyOrderController {

    @Autowired
    private PharmacyOrderService service;

    @Override
    public PharmacyOrderDto createPharmacyOrder(PharmacyOrderDto request) {
        return service.createProduct(request);
    }

    @Override
    public PharmacyOrderDto readPharmacyOrder(String id) {
        return service.readProduct(id);
    }

    @Override
    public PharmacyOrderDto patchPharmacyOrder(String id, PharmacyOrderPatchDto patchDto) {
        return service.pharmacyOrderPatch(id, patchDto);
    }

    @Override
    public PageResponse filterPharmacyOrder(LocalDate stDate, LocalDate edDate, String patientName, String uhId, String statuses, String prescriptionId, int page, int size) {
        return service.filterPharmacyOrder(stDate, edDate, patientName, uhId, statuses, prescriptionId, page, size);
    }
}
