package com.ssf.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.product.constant.MockDataConstant;
import com.ssf.product.constant.ProductTypeEnum;
import com.ssf.product.dtos.ProductDto;
import com.ssf.product.dtos.ProductPatchDto;
import com.ssf.product.entities.Product;
import com.ssf.product.exception.EntityNotFoundException;
import com.ssf.product.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
@Disabled
public class ProductServiceTest {

    @Autowired
    private ProductService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IProductRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreateProduct() {
        //Mock
        ProductDto request = MockDataConstant.buildProductDto();

        Product product = mapper.map(request, Product.class);

        Mockito.when(repository.save(product)).thenReturn(product);
        request = service.createProduct(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadProduct() {
        ProductDto request = MockDataConstant.buildProductDto();

        Product product = mapper.map(request, Product.class);
        when(repository.findById(MockDataConstant.PRODUCT_ID)).thenReturn(Optional.of(product));

        // call service
        ProductDto actualResponse = service.readProduct(MockDataConstant.PRODUCT_ID);
        assertThat(actualResponse.getName()).isEqualTo(MockDataConstant.PRODUCT_NAME);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadProduct() {
        when(repository.findById(MockDataConstant.PRODUCT_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readProduct(MockDataConstant.PRODUCT_ID));
    }

    @Test
    public void whenValid_ThenPatchProduct() {
        ProductDto request = MockDataConstant.buildProductDto();
        Map<String, Object> jsonReq = objectMapper.convertValue(request, Map.class);
        Product product = mapper.map(request, Product.class);
        when(repository.findById(MockDataConstant.PRODUCT_ID)).thenReturn(Optional.of(product));
        Mockito.when(repository.save(product)).thenReturn(product);
        ProductDto result = service.patchProduct(MockDataConstant.PRODUCT_ID, jsonReq);
        assertThat(result).isNotNull();
    }
}
