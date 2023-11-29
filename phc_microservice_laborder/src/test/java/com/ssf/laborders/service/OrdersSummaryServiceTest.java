package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.constants.Genders;
import com.ssf.laborders.dtos.AuditDTO;
import com.ssf.laborders.entities.LabOrder;
import com.ssf.laborders.entities.Patient;
import com.ssf.laborders.repository.LabOrdersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class OrdersSummaryServiceTest {

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    IOrdersSummaryService service;

    @Autowired
    LabOrdersRepository labOrdersRepository;

    @Test
    void getOrdersSummaryByFilterByDate() {
        assertNotNull(service.getOrdersSummaryByFilter("2022-07-14", "2022-07-14", 0, 5));
    }

    @Test
    void getOrdersSummaryByFilter() {
        assertNotNull(service.getOrdersSummaryByFilter(null, null, 0, 5));
    }

    @Test
    void updateOrdersSummary_Male() throws Exception {
        File file = ResourceUtils.getFile("classpath:lab-order-sample.json");
        LabOrder labOrders = mapper.readValue(file, LabOrder.class);
        labOrders.setAudit(AuditDTO.builder().createdBy("").modifiedBy("").build());
        labOrdersRepository.save(labOrders);
        assertNotNull(service.updateOrdersSummary(labOrders));
    }

    @Test
    void updateOrdersSummary_Female() throws Exception {
        File file = ResourceUtils.getFile("classpath:lab-order-sample.json");
        LabOrder labOrders = mapper.readValue(file, LabOrder.class);
        labOrders.setAudit(AuditDTO.builder().createdBy("").modifiedBy("").build());
        Patient patient = labOrders.getPatient();
        patient.setGender(Genders.Female);
        labOrders.setPatient(patient);
        labOrdersRepository.save(labOrders);
        assertNotNull(service.updateOrdersSummary(labOrders));
    }

    @Test
    void updateOrdersSummary_TransGender() throws Exception {
        File file = ResourceUtils.getFile("classpath:lab-order-sample.json");
        LabOrder labOrders = mapper.readValue(file, LabOrder.class);
        labOrders.setAudit(AuditDTO.builder().createdBy("").modifiedBy("").build());
        Patient patient = labOrders.getPatient();
        patient.setGender(Genders.Transgender);
        labOrders.setPatient(patient);
        labOrdersRepository.save(labOrders);
        assertNotNull(service.updateOrdersSummary(labOrders));
    }


}