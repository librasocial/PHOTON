package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.LarvaVisitsFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Larva;
import com.ssf.surveillance.entities.LarvaVisits;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class LarvaVisitsServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    ILarvaService larvaService;

    @Autowired
    ILarvaVisitsService service;

    private Larva larva;

    private LarvaVisits larvaVisits;

    @Test
    @BeforeEach
    void testCreateLarva() throws Exception {
        File file = ResourceUtils.getFile("classpath:larva-sample.json");
        Larva larva = mapper.readValue(file, Larva.class);
        SurveillancePageResponse response = larvaService.createLarvaSurveillance(larva);
        assertNotNull(response);
        this.larva = ((List<Larva>) response.getContent()).get(0);

        file = ResourceUtils.getFile("classpath:larva-visits-sample.json");
        LarvaVisits larvaVisits = mapper.readValue(file, LarvaVisits.class);
        larvaVisits.setLarvaSurveillanceId(this.larva.getId());
        response = service.createLarvaSurveillanceVisits(this.larva.getId(), larvaVisits);
        assertNotNull(response);
        this.larvaVisits = ((List<LarvaVisits>) response.getContent()).get(0);


    }

    @Test
    void testCreateLarva_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.createLarvaSurveillanceVisits("string", this.larvaVisits);
        });
    }


    @Test
    void testGetLarva() {
        assertNotNull(service.getLarvaSurveillanceVisits(this.larva.getId(), this.larvaVisits.getId()));
    }

    @Test
    void testUpdateLarva() {
        assertNotNull(service.updateLarvaSurveillanceVisits(this.larva.getId(), this.larvaVisits.getId(), new HashMap<>()));
    }

    @Test
    void testUpdateLarva_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateLarvaSurveillanceVisits("string", "string", new HashMap<>());
        });
    }

    @Test
    void testUpdateLarva_NoElementVisit() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateLarvaSurveillanceVisits(this.larva.getId(), "string", new HashMap<>());
        });
    }

    @Test
    void testGetLarvaByFilters() {
        LarvaVisitsFilterDTO larvaVisitsFilterDTO = LarvaVisitsFilterDTO.builder().page(0).size(5).surveillanceId(this.larva.getId()).build();
        assertNotNull(service.getLarvaSurveillanceVisitsByFilter(larvaVisitsFilterDTO));
    }


}