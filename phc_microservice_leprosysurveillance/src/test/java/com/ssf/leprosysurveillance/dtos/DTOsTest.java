package com.ssf.leprosysurveillance.dtos;

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
import com.ssf.leprosysurveillance.dto.AuditDto;
import com.ssf.leprosysurveillance.dto.LabResultDto;
import com.ssf.leprosysurveillance.dto.LeprosySurveillanceDto;
import com.ssf.leprosysurveillance.dto.PastDto;
import com.ssf.leprosysurveillance.dto.SampleDto;
import com.ssf.leprosysurveillance.model.LeprosySurveillance;

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
	void testLeprosySurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillanceDto leprosySurveillanceDtoFromFile = mapper.readValue(file, LeprosySurveillanceDto.class);
		LeprosySurveillanceDto leprosySurveillanceDto = LeprosySurveillanceDto.builder().id("random-uuid")
				.citizenId("string").dateOfSurveillance(LocalDateTime.now()).isNewlySuspected(false)
				.suspectedType(leprosySurveillanceDtoFromFile.toString()).symptoms(null).isReferredToPhc(false)
				.past(leprosySurveillanceDtoFromFile.getPast()).sample(leprosySurveillanceDtoFromFile.getSample())
				.labResult(leprosySurveillanceDtoFromFile.getLabResult())
				.audit(leprosySurveillanceDtoFromFile.getAudit()).build();
		LeprosySurveillanceDto leprosySurveillanceDtoCopy = new LeprosySurveillanceDto();
		BeanUtils.copyProperties(leprosySurveillanceDto, leprosySurveillanceDtoCopy);
		assertEquals(leprosySurveillanceDto, leprosySurveillanceDtoCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsHascodeLeprosySurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillanceDto leprosySurveillanceDtoFromFile = mapper.readValue(file, LeprosySurveillanceDto.class);
		LeprosySurveillanceDto leprosySurveillanceDtoCopy = new LeprosySurveillanceDto();
		assertNotEquals(Integer.toString(leprosySurveillanceDtoFromFile.hashCode()),
				Integer.toString(leprosySurveillanceDtoCopy.hashCode()));
	}

	@Test
	@SneakyThrows
	void testNotEqualsLeprosySampleSurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillanceDto leprosySurveillanceFromFile = mapper.readValue(file, LeprosySurveillanceDto.class);
		LeprosySurveillanceDto leprosySurveillanceDtoCopy = new LeprosySurveillanceDto();
		assertNotEquals(leprosySurveillanceFromFile.equals(leprosySurveillanceDtoCopy), true);
	}

	@Test
	@SneakyThrows
	void testLeprosySurveillance() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillance leprosySurveillance = mapper.readValue(file, LeprosySurveillance.class);
		leprosySurveillance.setCitizenId(leprosySurveillance.toString());
		LeprosySurveillance leprosySurveillanceCopy = new LeprosySurveillance();

		BeanUtils.copyProperties(leprosySurveillance, leprosySurveillanceCopy);
		assertEquals(leprosySurveillance, leprosySurveillanceCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsLeprosySurveillance() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillance leprosySurveillanceFromFile = mapper.readValue(file, LeprosySurveillance.class);
		LeprosySurveillance leprosySurveillanceCopy = new LeprosySurveillance();
		assertNotEquals(Integer.toString(leprosySurveillanceFromFile.hashCode()),
				Integer.toString(leprosySurveillanceCopy.hashCode()));
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
		LabResultDto labResultDto = LabResultDto.builder().resultDate(LocalDateTime.now()).result("string")
				.reportImages(null).build();
		BeanUtils.copyProperties(labResultDto, labResultDtoCopy);
		assertEquals(labResultDto, labResultDtoCopy);
	}

	@Test
	void testSampleDto() {
		SampleDto sampleDtoCopy = new SampleDto();
		SampleDto sampleDto = SampleDto.builder().isSampleCollected("string").sampleCollectedDate(LocalDateTime.now())
				.labReceivedDate(LocalDateTime.now()).build();
		BeanUtils.copyProperties(sampleDto, sampleDtoCopy);
		assertEquals(sampleDto, sampleDtoCopy);
	}

	@Test
	void testPastDto() {
		PastDto pastDtoCopy = new PastDto();
		PastDto pastDto = PastDto.builder().wasConfirmed(false).result("string").hasUndergoneRCSSurgery(null).build();
		BeanUtils.copyProperties(pastDto, pastDtoCopy);
		assertEquals(pastDto, pastDtoCopy);
	}
}
