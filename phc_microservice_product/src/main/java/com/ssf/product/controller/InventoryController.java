package com.ssf.product.controller;

import com.ssf.product.dtos.InventoryDto;
import com.ssf.product.dtos.PageResponse;
import com.ssf.product.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class InventoryController implements IInventoryController {
    @Autowired
    private InventoryService service;
    @Override
    public InventoryDto createInventory(String productId, InventoryDto request) {
        return service.createInventory(productId, request);
    }

    @Override
    public InventoryDto readInventory(String productId, String id) {
        return service.readInventory(productId, id);
    }

    @Override
    public InventoryDto patchInventory(String productId, String id, Map<String, Object> request) {
        return service.patchInventory(productId,id, request);
    }

    @Override
    public PageResponse getInventoryByFilter(String searchStr, String productId, String batchNumber, Integer page, Integer size) {
        return service.getInventoryByFilter(searchStr, productId, batchNumber, page, size);
    }
}
