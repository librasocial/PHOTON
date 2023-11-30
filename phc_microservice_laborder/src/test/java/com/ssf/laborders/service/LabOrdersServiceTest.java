package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.constants.LabOrderTypeEnum;
import com.ssf.laborders.dtos.LabOrderDTO;
import com.ssf.laborders.dtos.LabOrderPatchDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class LabOrdersServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    ILabOrdersService service;

    @Test
    void getHealth() {
        assertNotNull(service.getHealth());
    }

    @SneakyThrows
    @Test
    void createLabOrder() {
        File file = ResourceUtils.getFile("classpath:lab-order-sample.json");
        LabOrderDTO labOrderDTO = mapper.readValue(file, LabOrderDTO.class);
        assertNotNull(service.createLabOrder(labOrderDTO));
    }

    @Test
    void getLabOrders() {
        assertNotNull(service.getLabOrders("string"));
    }

    @SneakyThrows
    @Test
    void updateLabOrders() {
        LabOrderPatchDTO labOrderPatchDTO = LabOrderPatchDTO.builder()
                .type(LabOrderTypeEnum.LabOrders).properties(new HashMap<>()).build();
        assertNotNull(service.updateLabOrders("string", labOrderPatchDTO));
    }

    @Test
    void updateLabOrders_Encounter() {
        LabOrderPatchDTO labOrderPatchDTO = LabOrderPatchDTO.builder()
                .type(LabOrderTypeEnum.Encounter).properties(new HashMap<>()).build();
        assertNotNull(service.updateLabOrders("string", labOrderPatchDTO));
    }

    @Test
    void updateLabOrders_Patient() {
        LabOrderPatchDTO labOrderPatchDTO = LabOrderPatchDTO.builder()
                .type(LabOrderTypeEnum.Patient).properties(new HashMap<>()).build();
        assertNotNull(service.updateLabOrders("string", labOrderPatchDTO));
    }


    @Test
    void getLabOrdersByFilterByDate() {
        assertNotNull(service.getLabOrdersByFilter("2022-07-14", "2022-07-14", "", "", "", "", 0, 5));
    }

    @Test
    void getLabOrdersByFilter() {
        assertNotNull(service.getLabOrdersByFilter(null, null, null, null, null, null, 0, 5));
    }


}