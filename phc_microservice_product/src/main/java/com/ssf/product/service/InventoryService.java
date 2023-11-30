package com.ssf.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.product.dtos.AuditDto;
import com.ssf.product.dtos.InventoryDto;
import com.ssf.product.dtos.PageDto;
import com.ssf.product.dtos.PageResponse;
import com.ssf.product.entities.Inventory;
import com.ssf.product.exception.EntityNotFoundException;
import com.ssf.product.repository.IInventoryRepository;
import com.ssf.product.repository.IProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private IInventoryRepository repository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuditorAware<String> auditorProvider;
    @Autowired
    private ObjectMapper objectMapper;

    public InventoryDto createInventory(String productId, InventoryDto request) {
        log.debug("Creating the inventory");
        boolean isProductExist = productRepository.existsById(productId);
        if(!isProductExist)
            throw new EntityNotFoundException("Product Id not found "+productId);

        Inventory inventory = mapper.map(request, Inventory.class);
        repository.save(inventory);
        InventoryDto response = mapper.map(inventory, InventoryDto.class);

        // Audit save
        AuditDto auditDto = buildAuditDto(inventory);
        response.setAudit(auditDto);
        return response;
    }

    public InventoryDto readInventory(String  productId, String id) {
        log.debug("Reading the Inventory for inventoryId {}", id);
        auditorProvider.getCurrentAuditor();
        Inventory inventory = getInventory(productId, id);
        InventoryDto response = mapper.map(inventory, InventoryDto.class);

        //Audit save
        AuditDto auditDto = buildAuditDto(inventory);
        response.setAudit(auditDto);

        return response;

    }

    public InventoryDto patchInventory(String productId, String id, Map<String, Object> request) {
        log.debug("Updating the inventory for  id {}", id);
        boolean isProductExist = productRepository.existsById(productId);
        if(!isProductExist)
            throw new EntityNotFoundException("Product Id not found "+productId);

        InventoryDto inventoryDto = objectMapper.convertValue(request, InventoryDto.class);

        Inventory existingInventory = getInventory(productId, id);
        Map<String, Object> dbMap = objectMapper.convertValue(existingInventory, Map.class);
        dbMap.putAll(request);
        Inventory data = objectMapper.convertValue(dbMap, Inventory.class);
        repository.save(data);

        //response generate
        var response = mapper.map(data, InventoryDto.class);
        //Audit save
        AuditDto auditDto = buildAuditDto(existingInventory);
        response.setAudit(auditDto);
        return response;
    }
    public PageResponse getInventoryByFilter(String searchStr, String productId, String batchNumber, Integer page, Integer size) {
        log.debug("filter the Inventory for searchStr {}, {}, {} ", searchStr, productId, batchNumber);
        auditorProvider.getCurrentAuditor();
        Pageable paging = PageRequest.of(page, size);
        Inventory example = Inventory.builder().build();
        if (StringUtils.isNotBlank(searchStr)) {
            example.setName(searchStr);
        }
        if (StringUtils.isNotBlank(productId)) {
            example.setProductId(productId);
        }
        if (StringUtils.isNotBlank(batchNumber)) {
            example.setBatchNumber(batchNumber);
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name",
                        ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
        Page<Inventory> inventoryPage = repository.findAll(Example.of(example, matcher), paging);
        return buildPageResponse(inventoryPage);
    }

    public Inventory getInventory(String productId, String id) {
        return repository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new EntityNotFoundException("inventory ::" + id + " not found"));
    }

    private PageResponse buildPageResponse(Page<Inventory> inventorys) {
        if (inventorys == null || inventorys.isEmpty()) {
            log.debug(" Pagination result is null ");
            return PageResponse.builder()
                    .meta(PageDto.builder().totalPages(0L).totalElements(0L).number(0).size(0).build())
                    .data(new ArrayList<>())
                    .build();

        } else {
            log.debug(" Pagination result is returned " + inventorys.getTotalElements());
            Page<InventoryDto> dtos = buildInventoryDtos(inventorys);
            return PageResponse.builder()
                    .meta(PageDto.builder()
                            .totalPages((long) inventorys.getTotalPages())
                            .totalElements(inventorys.getTotalElements())
                            .number(inventorys.getNumber())
                            .size(inventorys.getSize())
                            .build())
                    .data(dtos.toList())
                    .build();
        }
    }

    private Page<InventoryDto> buildInventoryDtos(Page<Inventory> inventorys) {
        return inventorys.map(p -> {
            // Conversion logic
            InventoryDto inventoryDto = mapper.map(p, InventoryDto.class);
            //Audit save
            AuditDto auditDto = buildAuditDto(p);
            inventoryDto.setAudit(auditDto);
            return inventoryDto;
        });
    }
    private AuditDto buildAuditDto(Inventory inventory) {
        return AuditDto.builder().createdBy(inventory.getCreatedBy())
                .modifiedBy(inventory.getModifiedBy())
                .dateCreated(inventory.getDateCreated())
                .dateModified(inventory.getDateModified())
                .build();
    }
}
