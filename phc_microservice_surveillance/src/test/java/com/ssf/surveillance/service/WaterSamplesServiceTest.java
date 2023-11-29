package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.dtos.WaterSamplesFilterDTO;
import com.ssf.surveillance.entities.WaterSamples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class WaterSamplesServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    IWaterSamplesService service;

    private WaterSamples waterSamples;

    @Test
    @BeforeEach
    void testCreateWaterSamples() throws Exception {
        File file = ResourceUtils.getFile("classpath:water-sample.json");
        WaterSamples waterSamples = mapper.readValue(file, WaterSamples.class);
        SurveillancePageResponse response = service.createWaterSamplesSurveillance(waterSamples);
        assertNotNull(response);
        this.waterSamples = ((List<WaterSamples>) response.getContent()).get(0);
    }


    @Test
    void testGetWaterSamples() {
        assertNotNull(service.getWaterSamplesSurveillance(this.waterSamples.getId()));
    }

    @Test
    void testUpdateWaterSamples() throws Exception {
        File file = ResourceUtils.getFile("classpath:water-sample.json");
        assertNotNull(service.updateWaterSamplesSurveillance(this.waterSamples.getId(), mapper.readValue(file, HashMap.class)));
    }

    @Test
    void testUpdateWaterSamples_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateWaterSamplesSurveillance("string", new HashMap<>());
        });
    }

    @Test
    void testGetWaterSamplesByFilters() {
        WaterSamplesFilterDTO waterSamplesFilterDTO = WaterSamplesFilterDTO.builder().villageId(Optional.of(""))
                .householdId(Optional.of("")).placeType(Optional.of("")).placeOrgId(Optional.of(""))
                .dateOfSurvey(Optional.of("2022-08-05"))
                .page(0).size(10).build();
        assertNotNull(service.getWaterSamplesSurveillanceByFilter(waterSamplesFilterDTO));
    }


}