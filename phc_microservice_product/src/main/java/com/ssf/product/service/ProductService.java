package com.ssf.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.product.dtos.*;
import com.ssf.product.entities.Inventory;
import com.ssf.product.entities.Product;
import com.ssf.product.entities.Uom;
import com.ssf.product.exception.EntityNotFoundException;
import com.ssf.product.repository.IProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private IProductRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    public ProductDto createProduct(ProductDto request) {
        log.debug("Creating the product");

        Product product = mapper.map(request, Product.class);
        repository.save(product);
        ProductDto response = mapper.map(product, ProductDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(product);
        response.setAudit(auditDto);
        return response;
    }

    public ProductDto readProduct(String id) {
        log.debug("Reading the Product for productId {}", id);
        auditorProvider.getCurrentAuditor();
        Product product = getProduct(id);
        ProductDto response = mapper.map(product, ProductDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(product);
        response.setAudit(auditDto);

        return response;

    }

    public ProductDto patchProduct(String id, Map<String, Object> request) {
        log.debug("Updating the product for  id {}", id);
        ProductDto productDto = objectMapper.convertValue(request, ProductDto.class);

        Product existingProduct = getProduct(id);
        Map<String, Object> dbMap = objectMapper.convertValue(existingProduct, Map.class);

        if (request.containsKey("umo")) {
            List<Uom> newUmo = (List<Uom>) request.get("umo");
            existingProduct.setUmo(newUmo);
        }
        dbMap.putAll(request);
        Product data = objectMapper.convertValue(dbMap, Product.class);
        repository.save(data);

        //response generate
        var response = mapper.map(data, ProductDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(existingProduct);
        response.setAudit(auditDto);
        return response;
    }
    public PageResponse getProductByFilter(String searchStr, Integer page, Integer size) {
        log.debug("filter the Product for searchStr {}", searchStr);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);

        if (StringUtils.isNotBlank(searchStr)) {
            return buildPageResponse(repository.findAllByNameLikeIgnoreCase(searchStr,paging));
        }

        return buildPageResponse(repository.findAll(paging));

    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("product ::" + id + " not found"));
    }

    private PageResponse buildPageResponse(Page<Product> products) {
        if (products == null || products.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + products.getTotalElements());
            Page<ProductDto> dtos = buildProductDtos(products);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) products.getTotalPages())
                            .totalElements(products.getTotalElements())
                            .number(products.getNumber())
                            .size(products.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }

    private Page<ProductDto> buildProductDtos(Page<Product> products) {
        return products.map(p -> {
            // Conversion logic
            ProductDto productDto = mapper.map(p, ProductDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            productDto.setAudit(auditDto);
            return productDto;
        });
    }
    private AuditDto buildAuditDto(Product product) {
        return AuditDto.builder().createdBy(product.getCreatedBy())
                .modifiedBy(product.getModifiedBy())
                .dateCreated(product.getDateCreated())
                .dateModified(product.getDateModified())
                .build();
    }
}
