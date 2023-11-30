package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.constants.OrderSampleTypeEnum;
import com.ssf.laborders.dtos.LabOrdersPageResponse;
import com.ssf.laborders.dtos.OrderSamplesDTO;
import com.ssf.laborders.dtos.OrderSamplesPatchDTO;
import com.ssf.laborders.entities.OrderSamples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class OrderSamplesServiceTest {

    @Autowired
    IOrderSamplesService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    private OrderSamples orderSamples;

    @Test
    @BeforeEach
    void createOrderSamples() throws Exception {
        File file = ResourceUtils.getFile("classpath:order-sample.json");
        OrderSamplesDTO orderSamplesDTO = mapper.readValue(file, OrderSamplesDTO.class);
        LabOrdersPageResponse response = service.createOrderSamples(orderSamplesDTO);
        assertNotNull(response);
        this.orderSamples = ((List<OrderSamples>) response.getContent()).get(0);
    }

    @Test
    void getOrderSamples() {
        assertNotNull(service.getOrderSamples("string", "string"));
    }

    @Test
    void updateOrderSamples_whenNotFound() {

        OrderSamplesPatchDTO samplesPatchDTO = OrderSamplesPatchDTO.builder()
                .type(OrderSampleTypeEnum.OrderSamples).properties(new HashMap<>()).build();
        assertThrows(NoSuchElementException.class, () -> {
            service.updateOrderSamples("string", "string", samplesPatchDTO);
        });
    }

    @Test
    void updateOrderSamples_Sample() {
        OrderSamplesPatchDTO samplesPatchDTO = OrderSamplesPatchDTO.builder()
                .type(OrderSampleTypeEnum.Sample).properties(new HashMap<>()).build();
        assertNotNull(service.updateOrderSamples("string", orderSamples.getId(), samplesPatchDTO));
    }

    @Test
    void updateOrderSamples_OrderSample() {
        OrderSamplesPatchDTO samplesPatchDTO = OrderSamplesPatchDTO.builder()
                .type(OrderSampleTypeEnum.OrderSamples).properties(new HashMap<>()).build();
        assertNotNull(service.updateOrderSamples("string", orderSamples.getId(), samplesPatchDTO));
    }

    @Test
    void getOrderSamplesByFilter() {
        assertNotNull(service.getOrderSamplesByFilter("string", 0, 5));
    }
}