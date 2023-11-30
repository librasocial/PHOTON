package com.ssf.laborders.dtos;

import com.ssf.laborders.constants.LabOrderTypeEnum;
import com.ssf.laborders.constants.OrderSampleTypeEnum;
import com.ssf.laborders.constants.OrderStatus;
import com.ssf.laborders.constants.TestResultTypeEnum;
import com.ssf.laborders.entities.Encounter;
import com.ssf.laborders.entities.Patient;
import com.ssf.laborders.entities.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class DTOsTest {

    @Test
    void testAuditDTO() {
        AuditDTO auditDTOCopy = new AuditDTO();
        AuditDTO auditDTO = AuditDTO.builder().modifiedBy("new").createdBy("new")
                .dateCreated(LocalDateTime.now()).dateModified(LocalDateTime.now()).build();
        BeanUtils.copyProperties(auditDTO, auditDTOCopy);
        assertEquals(auditDTO, auditDTOCopy);
    }

    @Test
    void testEncounterTest() {
        EncounterTest encounterTestCopy = new EncounterTest();
        EncounterTest encounterTest = EncounterTest.builder().encounterId("random-uuid").encounterDateTime(LocalDateTime.now())
                .staffId("random-uuid-1").staffName("new-staff")
                .build();
        BeanUtils.copyProperties(encounterTest, encounterTestCopy);
        assertEquals(encounterTest, encounterTestCopy);
    }

    @Test
    void testGenderCountDTO() {
        GenderCountDTO genderCountDTOCopy = new GenderCountDTO();
        GenderCountDTO genderCountDTO = GenderCountDTO.builder().id("Male").count(0L).build();
        BeanUtils.copyProperties(genderCountDTO, genderCountDTOCopy);
        assertEquals(genderCountDTO, genderCountDTOCopy);
    }

    @Test
    void testLabOrderDTO() {
        LabOrderDTO labOrderDTOCopy = new LabOrderDTO();
        LabOrderDTO labOrderDTO = LabOrderDTO.builder().id("random-uuid")
                .patient(new Patient()).assignedToLab("").isStat(false).status(OrderStatus.ORDER_ACCEPTED)
                .encounter(new Encounter()).originatedBy("").medicalTests(new ArrayList<>()).orderDate(LocalDateTime.now()).build();
        BeanUtils.copyProperties(labOrderDTO, labOrderDTOCopy);
        assertEquals(labOrderDTO, labOrderDTOCopy);
    }

    @Test
    void testLabOrderPatchDTO() {
        LabOrderPatchDTO labOrderPatchDTOCopy = new LabOrderPatchDTO();
        LabOrderPatchDTO labOrderPatchDTO = LabOrderPatchDTO.builder().type(LabOrderTypeEnum.LabOrders).properties(new HashMap<>()).build();
        BeanUtils.copyProperties(labOrderPatchDTO, labOrderPatchDTOCopy);
        assertEquals(labOrderPatchDTO, labOrderPatchDTOCopy);
    }

    @Test
    void testLabOrdersPageResponse() {
        LabOrdersPageResponse labOrdersPageResponseCopy = new LabOrdersPageResponse();
        LabOrdersPageResponse labOrdersPageResponse = LabOrdersPageResponse.builder().totalPages(0L).totalElements(0L).content(new Object()).build();
        BeanUtils.copyProperties(labOrdersPageResponse, labOrdersPageResponseCopy);
        assertEquals(labOrdersPageResponse, labOrdersPageResponseCopy);
    }

    @Test
    void testMedicalTestDTO() {
        MedicalTestDTO medicalTestDTOCopy = new MedicalTestDTO();
        MedicalTestDTO medicalTestDTO = MedicalTestDTO.builder().labTestName("").labTestId("").sampleType("").build();
        BeanUtils.copyProperties(medicalTestDTO, medicalTestDTOCopy);
        assertEquals(medicalTestDTO, medicalTestDTOCopy);
    }

    @Test
    void testOrderSampleDTO() {
        OrderSamplesDTO orderSamplesDTOCopy = new OrderSamplesDTO();
        OrderSamplesDTO orderSamplesDTO = OrderSamplesDTO.builder().orderId("").sample(new Sample()).id("").build();
        BeanUtils.copyProperties(orderSamplesDTO, orderSamplesDTOCopy);
        assertEquals(orderSamplesDTO, orderSamplesDTOCopy);
    }

    @Test
    void testOrderSamplePatchDTO() {
        OrderSamplesPatchDTO orderSamplesPatchCopy = new OrderSamplesPatchDTO();
        OrderSamplesPatchDTO orderSamplesPatchDTO = OrderSamplesPatchDTO.builder().type(OrderSampleTypeEnum.Sample).properties(new HashMap<>()).build();
        BeanUtils.copyProperties(orderSamplesPatchDTO, orderSamplesPatchCopy);
        assertEquals(orderSamplesPatchDTO, orderSamplesPatchCopy);
    }

    @Test
    void testTestResultDTO() {
        TestResultsDTO testResultsDTOCopy = new TestResultsDTO();
        TestResultsDTO testResultsDTO = TestResultsDTO.builder().id("").results(new ArrayList<>()).orderId("").orderSampleId("").sample(new Sample()).build();
        BeanUtils.copyProperties(testResultsDTO, testResultsDTOCopy);
        assertEquals(testResultsDTO, testResultsDTOCopy);
    }

    @Test
    void testTestResultPatchDTO() {
        TestResultsPatchDTO testResultsPatchDTOCopy = new TestResultsPatchDTO();
        TestResultsPatchDTO testResultsPatchDTO = TestResultsPatchDTO.builder().type(TestResultTypeEnum.TestResults).properties(new HashMap<>()).build();
        BeanUtils.copyProperties(testResultsPatchDTO, testResultsPatchDTOCopy);
        assertEquals(testResultsPatchDTO, testResultsPatchDTOCopy);
    }

}