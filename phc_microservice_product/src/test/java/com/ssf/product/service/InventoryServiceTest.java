package com.ssf.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.product.constant.MockDataConstant;
import com.ssf.product.dtos.InventoryDto;
import com.ssf.product.dtos.ProductDto;
import com.ssf.product.entities.Inventory;
import com.ssf.product.entities.Product;
import com.ssf.product.exception.EntityNotFoundException;
import com.ssf.product.repository.IInventoryRepository;
import com.ssf.product.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class InventoryServiceTest {

    @Autowired
    private InventoryService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IProductRepository productRepository;
    @MockBean
    private IInventoryRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreateProductInventory() {
        //Mock
        InventoryDto request = MockDataConstant.buildInventoryDto();
        Inventory inventory = mapper.map(request, Inventory.class);
        Mockito.when(productRepository.existsById(MockDataConstant.PRODUCT_ID)).thenReturn(true);
        Mockito.when(repository.save(inventory)).thenReturn(inventory);
        request = service.createInventory(MockDataConstant.PRODUCT_ID,request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadProductInventory() {
        //Mock
        InventoryDto request = MockDataConstant.buildInventoryDto();
        Inventory inventory = mapper.map(request, Inventory.class);

        when(repository.findByIdAndProductId(MockDataConstant.INVENTORY_ID, MockDataConstant.PRODUCT_ID)).thenReturn(Optional.of(inventory));

        // call service
        InventoryDto actualResponse = service.readInventory(MockDataConstant.PRODUCT_ID, MockDataConstant.INVENTORY_ID);
        assertThat(actualResponse.getName()).isEqualTo(MockDataConstant.PRODUCT_NAME);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadProductInventory() {
        when(repository.findByIdAndProductId(MockDataConstant.INVENTORY_ID, MockDataConstant.PRODUCT_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readInventory(MockDataConstant.PRODUCT_ID, MockDataConstant.INVENTORY_ID));
    }

    @Test
    public void whenValid_ThenPatchProductInventory() {
        //Mock
        InventoryDto request = MockDataConstant.buildInventoryDto();
        Inventory inventory = mapper.map(request, Inventory.class);
        Mockito.when(productRepository.existsById(MockDataConstant.PRODUCT_ID)).thenReturn(true);
        when(repository.findByIdAndProductId(MockDataConstant.INVENTORY_ID, MockDataConstant.PRODUCT_ID)).thenReturn(Optional.of(inventory));


        Map<String, Object> jsonReq = objectMapper.convertValue(request, Map.class);
        InventoryDto result = service.patchInventory(MockDataConstant.PRODUCT_ID, MockDataConstant.INVENTORY_ID, jsonReq);
        assertThat(result).isNotNull();
    }
}
