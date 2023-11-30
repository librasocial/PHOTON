package com.ssf.laborders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.constants.TestResultTypeEnum;
import com.ssf.laborders.dtos.AuditDTO;
import com.ssf.laborders.dtos.TestResultsPatchDTO;
import com.ssf.laborders.entities.TestResults;
import com.ssf.laborders.repository.TestResultsRepository;
import com.ssf.laborders.service.ITestResultsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestResultsController.class)
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class TestResultsControllerTest {

    static ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    @Autowired
    private TestResultsRepository testResultsRepository;

    @Autowired
    private ITestResultsService service;

    @Autowired
    private MockMvc mockMvc;

    private TestResults testResults;

    @BeforeEach
    void setUp() throws Exception {
        File file = ResourceUtils.getFile("classpath:order-sample.json");
        TestResults testResults = mapper.readValue(file, TestResults.class);
        testResults.setAudit(AuditDTO.builder().createdBy("").modifiedBy("").build());
        testResults = testResultsRepository.save(testResults);
        this.testResults = testResults;
    }

    @Test
    void createLabOrder() throws Exception {
        mockMvc.perform(post("/laborders/string/results")
                        .content(mapper.writeValueAsString(this.testResults))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTestResults() throws Exception {
        mockMvc.perform(get("/laborders/{orderId}/results/{resultId}", "string", this.testResults.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateTestResults() throws Exception {
        TestResultsPatchDTO patchDTO = TestResultsPatchDTO.builder().type(TestResultTypeEnum.TestResults).properties(new HashMap<>()).build();
        mockMvc.perform(patch("/laborders/{orderId}/results/{resultId}", "string", this.testResults.getId())
                        .content(mapper.writeValueAsString(patchDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTestResultsByFilter() throws Exception {
        mockMvc.perform(get("/laborders/{orderId}/results/filter", "string")
                        .content(mapper.writeValueAsString(this.testResults))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}