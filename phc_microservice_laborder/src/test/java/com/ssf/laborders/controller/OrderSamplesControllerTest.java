package com.ssf.laborders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.constants.OrderSampleTypeEnum;
import com.ssf.laborders.dtos.OrderSamplesDTO;
import com.ssf.laborders.dtos.OrderSamplesPatchDTO;
import com.ssf.laborders.service.IOrderSamplesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OrderSamplesController.class)
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class OrderSamplesControllerTest {

    @Autowired
    IOrderSamplesService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createLabOrder() throws Exception {

        File file = ResourceUtils.getFile("classpath:order-sample.json");
        OrderSamplesDTO orderSamplesDTO = mapper.readValue(file, OrderSamplesDTO.class);

        mockMvc.perform(post("/laborders/string/samples")
                        .content(mapper.writeValueAsString(orderSamplesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderSamples() throws Exception {
        mockMvc.perform(get("/laborders/string/samples/string")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateOrderSamples() throws Exception {
        OrderSamplesPatchDTO orderSamplesDTO = OrderSamplesPatchDTO.builder().type(OrderSampleTypeEnum.OrderSamples).properties(new HashMap<>()).build();
        mockMvc.perform(patch("/laborders/string/samples/string")
                        .content(mapper.writeValueAsString(orderSamplesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void updateOrderSamples_Sample() throws Exception {
        OrderSamplesPatchDTO orderSamplesDTO = OrderSamplesPatchDTO.builder().type(OrderSampleTypeEnum.Sample).properties(new HashMap<>()).build();
        mockMvc.perform(patch("/laborders/string/samples/string")
                        .content(mapper.writeValueAsString(orderSamplesDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().is5xxServerError()));
    }

    @Test
    void getOrderSamplesByFilter() throws Exception {
        mockMvc.perform(get("/laborders/string/samples/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}