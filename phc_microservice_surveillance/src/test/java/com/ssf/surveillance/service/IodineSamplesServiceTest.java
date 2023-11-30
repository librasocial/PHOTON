package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.IodineSamplesFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Iodine;
import com.ssf.surveillance.entities.IodineSamples;
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
class IodineSamplesServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();

    @Autowired
    IIodineService iodineService;

    @Autowired
    IIodineSamplesService service;

    private Iodine iodine;

    private IodineSamples iodineSamples;

    @Test
    @BeforeEach
    void testCreateIodineSample() throws Exception {
        File file = ResourceUtils.getFile("classpath:iodine-sample.json");
        Iodine iodine = mapper.readValue(file, Iodine.class);
        SurveillancePageResponse response = iodineService.createIodineSurveillance(iodine);
        assertNotNull(response);
        this.iodine = ((List<Iodine>) response.getContent()).get(0);

        file = ResourceUtils.getFile("classpath:iodine-sample-sample.json");
        IodineSamples iodineSamples = mapper.readValue(file, IodineSamples.class);
        iodineSamples.setIodineSurveillanceId(this.iodine.getId());
        response = service.createIodineSamples(this.iodine.getId(), iodineSamples);
        assertNotNull(response);
        this.iodineSamples = ((List<IodineSamples>) response.getContent()).get(0);

    }

    @Test
    void testCreateIodineSample_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.createIodineSamples("string", this.iodineSamples);
        });
    }


    @Test
    void testGetIodineSample() {
        assertNotNull(service.getIodineSamples(this.iodine.getId(), this.iodineSamples.getId()));
    }

    @Test
    void testUpdateIodineSample() {
        assertNotNull(service.updateIodineSamples(this.iodine.getId(), this.iodineSamples.getId(), new HashMap<>()));
    }

    @Test
    void testUpdateIodineSample_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateIodineSamples("string", "string", new HashMap<>());
        });
    }

    @Test
    void testUpdateIodineSample_NoElementVisit() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateIodineSamples(this.iodine.getId(), "string", new HashMap<>());
        });
    }

    @Test
    void testGetIodineSampleByFilters() {
        IodineSamplesFilterDTO iodineSamplesFilterDTO = IodineSamplesFilterDTO.builder().page(0).size(5).surveillanceId(this.iodine.getId()).build();
        assertNotNull(service.getIodineSamplesByFilter(iodineSamplesFilterDTO));
    }


}