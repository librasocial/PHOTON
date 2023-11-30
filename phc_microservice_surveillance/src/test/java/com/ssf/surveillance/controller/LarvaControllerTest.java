package com.ssf.surveillance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.entities.Larva;
import com.ssf.surveillance.service.ILarvaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(LarvaController.class)
@ActiveProfiles("test")
class LarvaControllerTest {

    @Autowired
    ILarvaService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createLarva() throws Exception {

        File file = ResourceUtils.getFile("classpath:larva-sample.json");
        Larva larva = mapper.readValue(file, Larva.class);
        mockMvc.perform(post("/surveillance/larvas")
                        .content(mapper.writeValueAsString(larva))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getLarva() throws Exception {
        mockMvc.perform(get("/surveillance/larvas/string")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateLarva() throws Exception {
        mockMvc.perform(patch("/surveillance/larvas/string")
                        .content(mapper.writeValueAsString(new HashMap<>()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getLarvaByFilter() throws Exception {
        mockMvc.perform(get("/surveillance/larvas/filter?dateOfSurvey=2022-08-05&villageId=string&placeOrgId=string-1&householdId=string&page=0&size=10&placeType=string")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}