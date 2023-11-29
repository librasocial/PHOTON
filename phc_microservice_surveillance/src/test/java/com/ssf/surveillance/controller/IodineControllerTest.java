package com.ssf.surveillance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.entities.Iodine;
import com.ssf.surveillance.service.IIodineService;
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
@Import(IodineController.class)
@ActiveProfiles("test")
class IodineControllerTest {

    @Autowired
    IIodineService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createIodine() throws Exception {

        File file = ResourceUtils.getFile("classpath:iodine-sample.json");
        Iodine iodine = mapper.readValue(file, Iodine.class);
        mockMvc.perform(post("/surveillance/iodines")
                        .content(mapper.writeValueAsString(iodine))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getIodine() throws Exception {
        mockMvc.perform(get("/surveillance/iodines/string")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateIodine() throws Exception {
        mockMvc.perform(patch("/surveillance/iodines/string")
                        .content(mapper.writeValueAsString(new HashMap<>()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getIodineByFilter() throws Exception {
        mockMvc.perform(get("/surveillance/iodines/filter?dateOfSurvey=2022-08-05&villageId=string&placeOrgId=string-1&householdId=string&page=0&size=10&placeType=string")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}