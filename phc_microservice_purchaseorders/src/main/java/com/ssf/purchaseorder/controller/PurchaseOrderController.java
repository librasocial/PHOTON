package com.ssf.purchaseorder.controller;

import com.ssf.purchaseorder.dtos.PageResponse;
import com.ssf.purchaseorder.dtos.PurchaseOrderDto;
import com.ssf.purchaseorder.dtos.PurchaseOrderPatchDto;
import com.ssf.purchaseorder.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/purchaseorders")
public class PurchaseOrderController implements IPurchaseOrderController {

    @Autowired
    private PurchaseOrderService service;

    @Override
    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto request) {
        return service.createPurchaseOrder(request);
    }

    @Override
    public PurchaseOrderDto readPurchaseOrder(String id) {
        return service.readPurchaseOrder(id);
    }

    @Override
    public PurchaseOrderDto patchPurchaseOrder(String id, PurchaseOrderPatchDto patchDto) {
        return service.purchaseOrderPatch(id, patchDto);
    }

    @Override
    public PageResponse filterPurchaseOrder(LocalDate stDate, LocalDate edDate, String poType, String supplierName, String statuses, int page, int size) {
        return service.filterPurchaseOrder(stDate, edDate, poType, supplierName, statuses, page, size);
    }
}
