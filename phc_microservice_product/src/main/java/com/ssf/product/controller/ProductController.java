package com.ssf.product.controller;

import com.ssf.product.dtos.PageResponse;
import com.ssf.product.dtos.ProductDto;
import com.ssf.product.dtos.ProductPatchDto;
import com.ssf.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController implements   IProductController {

    @Autowired
    private ProductService service;
    @Override
    public ProductDto createProduct(ProductDto request) {
        return service.createProduct(request);
    }

    @Override
    public ProductDto readProduct(String id) {
        return service.readProduct(id);
    }

    @Override
    public ProductDto patchProduct(String id, Map<String, Object> request) {
        return service.patchProduct(id,request);
    }

    @Override
    public PageResponse getProductByFilter(String searchStr, Integer page, Integer size) {
        return service.getProductByFilter(searchStr, page, size);
    }
}
