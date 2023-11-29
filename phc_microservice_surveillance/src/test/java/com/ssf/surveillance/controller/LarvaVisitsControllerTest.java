package com.ssf.surveillance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.entities.Larva;
import com.ssf.surveillance.entities.LarvaVisits;
import com.ssf.surveillance.repository.LarvaRepository;
import com.ssf.surveillance.repository.LarvaVisitsRepository;
import com.ssf.surveillance.service.ILarvaService;
import org.junit.jupiter.api.BeforeEach;
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
@Import(LarvaVisitsController.class)
@ActiveProfiles("test")
class LarvaVisitsControllerTest {

    @Autowired
    ILarvaService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LarvaRepository larvaRepository;

    @Autowired
    private LarvaVisitsRepository larvaVisitsRepository;

    private Larva larva;

    private LarvaVisits larvaVisits;

    @Test
    @BeforeEach
    void createLarvaAndVisits() throws Exception {

        File file = ResourceUtils.getFile("classpath:larva-sample.json");
        Larva larva = mapper.readValue(file, Larva.class);
        this.larva = larvaRepository.save(larva);

        file = ResourceUtils.getFile("classpath:larva-visits-sample.json");
        LarvaVisits larvaVisits = mapper.readValue(file, LarvaVisits.class);
        this.larvaVisits = larvaVisitsRepository.save(larvaVisits);
    }

    @Test
    void createLarvaVisit() throws Exception {
        File file = ResourceUtils.getFile("classpath:larva-visits-sample.json");
        LarvaVisits larvaVisits = mapper.readValue(file, LarvaVisits.class);
        larvaVisits.setLarvaSurveillanceId(this.larva.getId());
        mockMvc.perform(post("/surveillance/larvas/{surveillanceId}/visits", this.larva.getId())
                        .content(mapper.writeValueAsString(larvaVisits))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getLarvaVisit() throws Exception {
        mockMvc.perform(get("/surveillance/larvas/{surveillanceId}/visits/{visitId}", this.larva.getId(), this.larvaVisits.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateLarvaVisit() throws Exception {
        mockMvc.perform(patch("/surveillance/larvas/{surveillanceId}/visits/{visitId}", this.larva.getId(), this.larvaVisits.getId())
                        .content(mapper.writeValueAsString(new HashMap<>()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getLarvaVisitByFilter() throws Exception {
        mockMvc.perform(get("/surveillance/larvas/{surveillanceId}/visits/filter?page=0&size=5", this.larva.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}