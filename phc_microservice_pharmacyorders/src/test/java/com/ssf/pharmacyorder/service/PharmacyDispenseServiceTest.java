package com.ssf.pharmacyorder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.pharmacyorder.constant.MockDataConstant;
import com.ssf.pharmacyorder.dtos.DispenseDto;
import com.ssf.pharmacyorder.entities.Dispense;
import com.ssf.pharmacyorder.exception.EntityNotFoundException;
import com.ssf.pharmacyorder.repository.IPharmacyDispenseRepository;
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

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Disabled
@ActiveProfiles("test")
public class PharmacyDispenseServiceTest {

    @Autowired
    private PharmacyDispenseService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IPharmacyOrderRepository orderRepository;
    @MockBean
    private IPharmacyDispenseRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreateProductInventory() {
        //Mock
        DispenseDto request = MockDataConstant.buildDispenseDto();
        Dispense dispense = mapper.map(request, Dispense.class);
        Mockito.when(orderRepository.existsById(MockDataConstant.ORDER_ID)).thenReturn(true);
        Mockito.when(repository.save(dispense)).thenReturn(dispense);
        request = service.createDispense(MockDataConstant.ORDER_ID,request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadProductInventory() {
        //Mock
        DispenseDto request = MockDataConstant.buildDispenseDto();
        Dispense dispense = mapper.map(request, Dispense.class);

        when(repository.findByIdAndOrderId(MockDataConstant.DIS_ID, MockDataConstant.ORDER_ID)).thenReturn(Optional.of(dispense));

        // call service
        DispenseDto actualResponse = service.readDispense(MockDataConstant.ORDER_ID, MockDataConstant.DIS_ID);
        assertThat(actualResponse.getOrderId()).isEqualTo(MockDataConstant.ORDER_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadProductInventory() {
        when(repository.findByIdAndOrderId(MockDataConstant.DIS_ID, MockDataConstant.ORDER_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readDispense(MockDataConstant.ORDER_ID, MockDataConstant.DIS_ID));
    }

    @Test
    public void whenValid_ThenPatchProductInventory() {
        //Mock
        DispenseDto request = MockDataConstant.buildDispenseDto();
        Dispense dispense = mapper.map(request, Dispense.class);
        Mockito.when(orderRepository.existsById(MockDataConstant.ORDER_ID)).thenReturn(true);
        when(repository.findByIdAndOrderId(MockDataConstant.DIS_ID, MockDataConstant.ORDER_ID)).thenReturn(Optional.of(dispense));


        Map<String, Object> jsonReq = objectMapper.convertValue(request, Map.class);
        DispenseDto result = service.pharmacyDispensePatch(MockDataConstant.ORDER_ID, MockDataConstant.DIS_ID, jsonReq);
        assertThat(result).isNotNull();
    }
}
