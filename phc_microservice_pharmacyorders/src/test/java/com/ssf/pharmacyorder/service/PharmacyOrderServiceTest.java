package com.ssf.pharmacyorder.service;

import com.ssf.pharmacyorder.constant.MockDataConstant;
import com.ssf.pharmacyorder.dtos.PharmacyOrderDto;
import com.ssf.pharmacyorder.entities.PharmacyOrder;
import com.ssf.pharmacyorder.exception.EntityNotFoundException;
import com.ssf.pharmacyorder.repository.IPharmacyOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class PharmacyOrderServiceTest {

    @Autowired
    private PharmacyOrderService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IPharmacyOrderRepository repository;
    
    @Test
    public void whenValidInput_ThenCreateOrder() {
        //Mock
        PharmacyOrderDto request = MockDataConstant.buildPharmacyOrderDto();

        PharmacyOrder product = mapper.map(request, PharmacyOrder.class);

        Mockito.when(repository.save(product)).thenReturn(product);
        request = service.createProduct(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadOrder() {
        PharmacyOrderDto request = MockDataConstant.buildPharmacyOrderDto();

        PharmacyOrder product = mapper.map(request, PharmacyOrder.class);
        when(repository.findById(MockDataConstant.ORDER_ID)).thenReturn(Optional.of(product));

        // call service
        PharmacyOrderDto actualResponse = service.readProduct(MockDataConstant.ORDER_ID);
        assertThat(actualResponse.getId()).isEqualTo(MockDataConstant.ORDER_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadOrder() {
        when(repository.findById(MockDataConstant.ORDER_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readProduct(MockDataConstant.ORDER_ID));
    }
}
