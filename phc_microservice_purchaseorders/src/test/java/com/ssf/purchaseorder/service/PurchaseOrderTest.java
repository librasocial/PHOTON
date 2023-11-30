package com.ssf.purchaseorder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssf.purchaseorder.constant.MockDataConstant;
import com.ssf.purchaseorder.dtos.PurchaseOrderDto;
import com.ssf.purchaseorder.entities.PurchaseOrder;
import com.ssf.purchaseorder.exception.EntityNotFoundException;
import com.ssf.purchaseorder.repository.IPurchaseOrderRepository;
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
@Disabled
@ActiveProfiles("test")
public class PurchaseOrderTest {
    @Autowired
    private PurchaseOrderService service;
    @Autowired
    private ModelMapper mapper;
    @MockBean
    private IPurchaseOrderRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_ThenCreatePurchaseOrder() {
        //Mock
        PurchaseOrderDto request = MockDataConstant.buildPurchaseOrderDto();

        PurchaseOrder purchaseOrder = mapper.map(request, PurchaseOrder.class);

        Mockito.when(repository.save(purchaseOrder)).thenReturn(purchaseOrder);
        request = service.createPurchaseOrder(request);
        assertThat(request).isNotNull();
    }

    @Test
    public void whenValidId_thenReadPurchaseOrder() {
        PurchaseOrderDto request = MockDataConstant.buildPurchaseOrderDto();

        PurchaseOrder purchaseOrder = mapper.map(request, PurchaseOrder.class);
        when(repository.findById(MockDataConstant.PURCHASE_ORDER_ID)).thenReturn(Optional.of(purchaseOrder));

        // call service
        PurchaseOrderDto actualResponse = service.readPurchaseOrder(MockDataConstant.PURCHASE_ORDER_ID);
        assertThat(actualResponse.getId()).isEqualTo(MockDataConstant.PURCHASE_ORDER_ID);
    }

    @Test
    public void whenInValidId_thenEntityNotFoundReadProduct() {
        when(repository.findById(MockDataConstant.PURCHASE_ORDER_ID)).thenReturn(Optional.empty());
        // call service and throws
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.readPurchaseOrder(MockDataConstant.PURCHASE_ORDER_ID));
    }
}
