package com.ssf.tbsurveillance.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.tbsurveillance.model.TbSurveillance;

import lombok.SneakyThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class DTOsTest {

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Test
	@SneakyThrows
	void testTbSurveillanceDTO() {
		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
		TbSurveillanceDto tbSurveillanceDTOFromFile = mapper.readValue(file, TbSurveillanceDto.class);
		TbSurveillanceDto tbSurveillanceDTO = TbSurveillanceDto.builder().id("random-uuid")
				.citizenId(tbSurveillanceDTOFromFile.toString()).dateOfSurveillance(LocalDateTime.now())
				.wasTreatedForTBInPast(false).isReferredToPhc(false).hasDiabetes(false).hasTBSymptoms(false)
				.sample(tbSurveillanceDTOFromFile.getSample()).labResult(tbSurveillanceDTOFromFile.getLabResult())
				.audit(tbSurveillanceDTOFromFile.getAudit()).build();
		TbSurveillanceDto tbSurveillanceDtoCopy = new TbSurveillanceDto();

		String S = TbSurveillanceDto.builder().build().toString();

		BeanUtils.copyProperties(tbSurveillanceDTO, tbSurveillanceDtoCopy);
		assertEquals(tbSurveillanceDTO, tbSurveillanceDtoCopy);
	}

	@Test
	void testAuditDto() {
		AuditDto auditDtoCopy = new AuditDto();
		AuditDto auditDtO = AuditDto.builder().modifiedBy("new").createdBy("new").dateCreated(LocalDateTime.now())
				.dateModified(LocalDateTime.now()).build();
		BeanUtils.copyProperties(auditDtO, auditDtoCopy);
		assertEquals(auditDtO, auditDtoCopy);
	}

	@Test
	void testLabResultDto() {
		LabResultDto labResultDtoCopy = new LabResultDto();
		LabResultDto labResultDto = LabResultDto.builder().resultDate(LocalDateTime.now()).dmcTestResult("string")
				.naatTestResult("string").chestXRayTestResult("string").nameOfTechnician("string").labSerialNo("string")
				.result("string").reportImages(null).build();
		String test = LabResultDto.builder().toString();
		BeanUtils.copyProperties(labResultDto, labResultDtoCopy);
		assertEquals(labResultDto, labResultDtoCopy);
	}

	@Test
	void testSampleDto() {
		SampleDto sampleDtoCopy = new SampleDto();
		SampleDto sampleDto = SampleDto.builder().isSampleCollected("string").sampleCollectedDate(LocalDateTime.now())
				.sampleSubmittedDate(LocalDateTime.now()).sampleSentToLoc("string").labReceivedDate(LocalDateTime.now())
				.remarks("string").build();
		// String test = SampleDto.builder().toString();
		BeanUtils.copyProperties(sampleDto, sampleDtoCopy);
		assertEquals(sampleDto, sampleDtoCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsHascodeTbSurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
		TbSurveillanceDto TbSurveillanceFromFile = mapper.readValue(file, TbSurveillanceDto.class);
		TbSurveillanceDto tbSurveillanceDtoCopy = new TbSurveillanceDto();
		assertNotEquals(Integer.toString(TbSurveillanceFromFile.hashCode()),
				Integer.toString(tbSurveillanceDtoCopy.hashCode()));
	}

	@Test
	@SneakyThrows
	void testNotEqualsTbSurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
		TbSurveillanceDto tbSurveillanceDtoFromFile = mapper.readValue(file, TbSurveillanceDto.class);
		TbSurveillanceDto TbSurveillanceDtoCopy = new TbSurveillanceDto();
		assertNotEquals(tbSurveillanceDtoFromFile.equals(TbSurveillanceDtoCopy), true);
	}

	@Test
	@SneakyThrows
	void testTbSurveillance() {
		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
		TbSurveillance tbSurveillance = mapper.readValue(file, TbSurveillance.class);
		tbSurveillance.setCitizenId(tbSurveillance.toString());
		TbSurveillance tbSurveillanceCopy = new TbSurveillance();

		BeanUtils.copyProperties(tbSurveillance, tbSurveillanceCopy);
		assertEquals(tbSurveillance, tbSurveillanceCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsTbSurveillance() {
		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
		TbSurveillance tbSurveillanceFromFile = mapper.readValue(file, TbSurveillance.class);
		TbSurveillance tbSurveillanceCopy = new TbSurveillance();
		assertNotEquals(Integer.toString(tbSurveillanceFromFile.hashCode()),
				Integer.toString(tbSurveillanceCopy.hashCode()));
	}
}
