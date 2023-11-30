package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.LarvaFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Larva;
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
class LarvaServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    ILarvaService service;

    private Larva larva;

    @Test
    @BeforeEach
    void testCreateLarva() throws Exception {
        File file = ResourceUtils.getFile("classpath:larva-sample.json");
        Larva larva = mapper.readValue(file, Larva.class);
        SurveillancePageResponse response = service.createLarvaSurveillance(larva);
        assertNotNull(response);
        this.larva = ((List<Larva>) response.getContent()).get(0);
    }


    @Test
    void testGetLarva() {
        assertNotNull(service.getLarvaSurveillance(this.larva.getId()));
    }

    @Test
    void testUpdateLarva() {
        assertNotNull(service.updateLarvaSurveillance(this.larva.getId(), new HashMap<>()));
    }

    @Test
    void testUpdateLarva_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateLarvaSurveillance("string", new HashMap<>());
        });
    }

    @Test
    void testGetLarvaByFilters() {
        LarvaFilterDTO larvaFilterDTO = LarvaFilterDTO.builder().villageId(Optional.of(""))
                .householdId(Optional.of("")).placeType(Optional.of("")).placeOrgId(Optional.of(""))
                .dateOfSurvey(Optional.of("2022-08-05"))
                .page(0).size(10).build();
        assertNotNull(service.getLarvaSurveillanceByFilter(larvaFilterDTO));
    }


}