package com.ssf.surveillance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.surveillance.dtos.IodineFilterDTO;
import com.ssf.surveillance.dtos.SurveillancePageResponse;
import com.ssf.surveillance.entities.Iodine;
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
class IodineServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    IIodineService service;

    private Iodine iodine;

    @Test
    @BeforeEach
    void testCreateIodine() throws Exception {
        File file = ResourceUtils.getFile("classpath:iodine-sample.json");
        Iodine iodine = mapper.readValue(file, Iodine.class);
        SurveillancePageResponse response = service.createIodineSurveillance(iodine);
        assertNotNull(response);
        this.iodine = ((List<Iodine>) response.getContent()).get(0);
    }


    @Test
    void testGetIodine() {
        assertNotNull(service.getIodineSurveillance(this.iodine.getId()));
    }

    @Test
    void testUpdateIodine() {
        assertNotNull(service.updateIodineSurveillance(this.iodine.getId(), new HashMap<>()));
    }

    @Test
    void testUpdateIodine_NoElement() {
        assertThrows(NoSuchElementException.class, () -> {
            service.updateIodineSurveillance("string", new HashMap<>());
        });
    }

    @Test
    void testGetIodineByFilters() {
        IodineFilterDTO iodineFilterDTO = IodineFilterDTO.builder().villageId(Optional.of(""))
                .householdId(Optional.of("")).placeType(Optional.of("")).placeOrgId(Optional.of(""))
                .dateOfSurvey(Optional.of("2022-08-05"))
                .page(0).size(10).build();
        assertNotNull(service.getIodineSurveillanceByFilter(iodineFilterDTO));
    }


}