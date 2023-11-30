package com.ssf.laborders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.dtos.LabOrderDTO;
import com.ssf.laborders.dtos.LabOrderPatchDTO;
import com.ssf.laborders.service.ILabOrdersService;
import lombok.SneakyThrows;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(LabOrdersController.class)
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class LabOrdersControllerTest {

    @Autowired
    ILabOrdersService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getHealth() throws Exception {
        mockMvc.perform(get("/laborders/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createLabOrder() {
        File file = ResourceUtils.getFile("classpath:lab-order-sample.json");
        LabOrderDTO labOrderDTO = mapper.readValue(file, LabOrderDTO.class);
        mockMvc.perform(post("/laborders")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(labOrderDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getLabOrders() throws Exception {
        mockMvc.perform(get("/laborders/string")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateLabOrders() throws Exception {
        File file = ResourceUtils.getFile("classpath:lab-order-patch.json");
        LabOrderPatchDTO labOrderPatchDTO = mapper.readValue(file, LabOrderPatchDTO.class);
        mockMvc.perform(patch("/laborders/string")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(labOrderPatchDTO)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getLabOrdersFiltersByDate() throws Exception {
        mockMvc.perform(get("/laborders/filter?startDate=2022-07-15&endDate=2022-07-16")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getLabOrdersFilter() throws Exception {
        mockMvc.perform(get("/laborders/filter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenWrongDateFormatPassedGetLabOrdersFilter() throws Exception {
        mockMvc.perform(get("/laborders/filter?startDate=2022-07-15&endDate=2022-07-16T00:00:00.000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}