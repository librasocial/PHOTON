package com.ssf.surveillance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.entities.Iodine;
import com.ssf.surveillance.entities.IodineSamples;
import com.ssf.surveillance.repository.IodineRepository;
import com.ssf.surveillance.repository.IodineSamplesRepository;
import com.ssf.surveillance.service.IIodineSamplesService;
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
@Import(IodineSampleController.class)
@ActiveProfiles("test")
class IodineSamplesControllerTest {

    @Autowired
    IIodineSamplesService service;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IodineRepository iodineRepository;

    @Autowired
    private IodineSamplesRepository iodineSamplesRepository;

    private Iodine iodine;

    private IodineSamples iodineSamples;

    @Test
    @BeforeEach
    void createLarvaAndVisits() throws Exception {

        File file = ResourceUtils.getFile("classpath:iodine-sample.json");
        Iodine iodine = mapper.readValue(file, Iodine.class);
        this.iodine = iodineRepository.save(iodine);

        file = ResourceUtils.getFile("classpath:iodine-sample-sample.json");
        IodineSamples iodineSamples = mapper.readValue(file, IodineSamples.class);
        this.iodineSamples = iodineSamplesRepository.save(iodineSamples);
    }

    @Test
    void createIodineSamples() throws Exception {
        File file = ResourceUtils.getFile("classpath:iodine-sample-sample.json");
        IodineSamples iodineSamples = mapper.readValue(file, IodineSamples.class);
        iodineSamples.setIodineSurveillanceId(this.iodine.getId());
        mockMvc.perform(post("/surveillance/iodines/{surveillanceId}/samples", this.iodine.getId())
                        .content(mapper.writeValueAsString(iodineSamples))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getIodineSamples() throws Exception {
        mockMvc.perform(get("/surveillance/iodines/{surveillanceId}/samples/{sampleId}", this.iodine.getId(), this.iodineSamples.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateIodineSamples() throws Exception {
        mockMvc.perform(patch("/surveillance/iodines/{surveillanceId}/samples/{sampleId}", this.iodine.getId(), this.iodineSamples.getId())
                        .content(mapper.writeValueAsString(new HashMap<>()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getIodineSamplesByFilter() throws Exception {
        mockMvc.perform(get("/surveillance/iodines/{surveillanceId}/samples/filter?page=0&size=5", this.iodine.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}