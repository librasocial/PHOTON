package com.ssf.laborders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.laborders.constants.TestResultTypeEnum;
import com.ssf.laborders.dtos.TestResultsDTO;
import com.ssf.laborders.dtos.TestResultsPatchDTO;
import com.ssf.laborders.entities.TestResults;
import com.ssf.laborders.repository.TestResultsRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class TestResultsServiceTest {

    @Autowired
    TestResultsRepository testResultsRepository;

    @Autowired
    ITestResultsService service;
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
    private TestResults testResults;

    @BeforeEach
    @Test
    void createTestResults() throws Exception {
        File file = ResourceUtils.getFile("classpath:order-sample.json");
        TestResultsDTO testResultsDTO = mapper.readValue(file, TestResultsDTO.class);
        Object results = service.createTestResults(testResultsDTO);
        assertNotNull(results);
        this.testResults = ((List<TestResults>) service.createTestResults(testResultsDTO).getContent()).get(0);
    }

    @Test
    void getTestResults() {
        assertNotNull(service.getTestResults("string", "string"));
    }

    @Test
    void getTestResults_whenValuePresent() {
        assertTrue(((List) service.getTestResults("string", this.testResults.getId()).getContent()).size() > 0);
    }

    @Test
    void updateTestResults_WhenValuePresent() {
        TestResultsPatchDTO patchDTO = TestResultsPatchDTO.builder()
                .type(TestResultTypeEnum.TestResults).properties(new HashMap<>()).build();
        assertNotNull(service.updateTestResults("string", this.testResults.getId(), patchDTO));
    }

    @Test
    void updateTestResults_Sample_WhenValuePresent() {
        TestResultsPatchDTO patchDTO = TestResultsPatchDTO.builder()
                .type(TestResultTypeEnum.Sample).properties(new HashMap<>()).build();
        assertNotNull(service.updateTestResults("string", this.testResults.getId(), patchDTO));
    }

    @Test
    void updateTestResults() {
        TestResultsPatchDTO patchDTO = TestResultsPatchDTO.builder()
                .type(TestResultTypeEnum.TestResults).properties(new HashMap<>()).build();
        assertThrows(NoSuchElementException.class, () -> {
            service.updateTestResults("string", "string", patchDTO);
        });
    }


    @Test
    void getTestResultsByFilter() {
        assertNotNull(service.getTestResultsByFilter("string", 0, 5));
    }
}