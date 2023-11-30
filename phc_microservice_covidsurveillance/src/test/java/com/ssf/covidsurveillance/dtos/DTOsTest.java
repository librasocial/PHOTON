package com.ssf.covidsurveillance.dtos;

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
import com.ssf.covidsurveillance.entities.CovidSurveillance;

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
	void testCovidSurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
		CovidSurveillanceDto covidSurveillanceDtoFromFile = mapper.readValue(file, CovidSurveillanceDto.class);
		CovidSurveillanceDto covidSurveillanceDto = CovidSurveillanceDto.builder().id("random-uuid").citizenId("string")
				.dateOfSurveillance(LocalDateTime.now()).vaccineType(covidSurveillanceDtoFromFile.toString())
				.noOfDoses("string").wasPreviouslyDiagnosed(false).audit(covidSurveillanceDtoFromFile.getAudit())
				.build();
		CovidSurveillanceDto covidSurveillanceDtoCopy = new CovidSurveillanceDto();
		BeanUtils.copyProperties(covidSurveillanceDto, covidSurveillanceDtoCopy);
		assertEquals(covidSurveillanceDto, covidSurveillanceDtoCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsHascodeCovidSurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
		CovidSurveillanceDto covidSurveillanceDtoFromFile = mapper.readValue(file, CovidSurveillanceDto.class);
		CovidSurveillanceDto covidSurveillanceDtoCopy = new CovidSurveillanceDto();
		assertNotEquals(Integer.toString(covidSurveillanceDtoFromFile.hashCode()),
				Integer.toString(covidSurveillanceDtoCopy.hashCode()));
	}

	@Test
	@SneakyThrows
	void testNotEqualsCovidSurveillanceDto() {
		File file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
		CovidSurveillanceDto covidSurveillanceDtoFromFile = mapper.readValue(file, CovidSurveillanceDto.class);
		CovidSurveillanceDto covidSurveillanceDtoCopy = new CovidSurveillanceDto();
		assertNotEquals(covidSurveillanceDtoFromFile.equals(covidSurveillanceDtoCopy), true);
	}

	@Test
	@SneakyThrows
	void testCovidSurveillance() {
		File file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
		CovidSurveillance covidSurveillance = mapper.readValue(file, CovidSurveillance.class);
		covidSurveillance.setCitizenId(covidSurveillance.toString());
		CovidSurveillance covidSurveillanceCopy = new CovidSurveillance();

		BeanUtils.copyProperties(covidSurveillance, covidSurveillanceCopy);
		assertEquals(covidSurveillance, covidSurveillanceCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsCovidSurveillance() {
		File file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
		CovidSurveillance covidSurveillanceFromFile = mapper.readValue(file, CovidSurveillance.class);
		CovidSurveillance covidSurveillanceCopy = new CovidSurveillance();
		assertNotEquals(Integer.toString(covidSurveillanceFromFile.hashCode()),
				Integer.toString(covidSurveillanceCopy.hashCode()));
	}

	@Test
	void testAuditDto() {
		AuditDto auditDtoCopy = new AuditDto();
		AuditDto auditDtO = AuditDto.builder().modifiedBy("new").createdBy("new").dateCreated(LocalDateTime.now())
				.dateModified(LocalDateTime.now()).build();
		BeanUtils.copyProperties(auditDtO, auditDtoCopy);
		assertEquals(auditDtO, auditDtoCopy);
	}

}
