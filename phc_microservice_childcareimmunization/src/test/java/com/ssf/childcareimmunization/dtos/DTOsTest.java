package com.ssf.childcareimmunization.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.childcareimmunization.entities.Immunization;

import lombok.SneakyThrows;

@SpringBootTest
@ActiveProfiles("test")
//@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
//@EmbeddedKafka
public class DTOsTest {
	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Test
	@SneakyThrows
	void testImmunizationDto() {
		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
		ImmunizationDto immunizationCareDtoFromFile = mapper.readValue(file, ImmunizationDto.class);
		ImmunizationDto immunizationCareDto = ImmunizationDto.builder().id("random-uuid").childCitizenId("string")
				.registrationDate(null).ashaWorker(null).targetDiseaseName(null).description(null).series(null)
				.doseNumber(null).vaccines(immunizationCareDtoFromFile.getVaccines())
				.reaction(immunizationCareDtoFromFile.getReaction())
				.isCovidTestDone(immunizationCareDtoFromFile.getIsCovidTestDone())
				.isCovidResultPositive(immunizationCareDtoFromFile.getIsCovidResultPositive())
				.isILIExperienced(immunizationCareDtoFromFile.getIsILIExperienced())
				.didContactCovidPatient(immunizationCareDtoFromFile.getDidContactCovidPatient())
				.audit(immunizationCareDtoFromFile.getAudit()).build();
		ImmunizationDto immunizationDtoCopy = new ImmunizationDto();
		BeanUtils.copyProperties(immunizationCareDto, immunizationDtoCopy);
		assertEquals(immunizationCareDto, immunizationDtoCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsHascodetestImmunizationDto() {
		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
		ImmunizationDto immunizationCareDtoFromFile = mapper.readValue(file, ImmunizationDto.class);
		ImmunizationDto immunizationCareDtoCopy = new ImmunizationDto();
		assertNotEquals(Integer.toString(immunizationCareDtoFromFile.hashCode()),
				Integer.toString(immunizationCareDtoCopy.hashCode()));
	}

	@Test
	@SneakyThrows
	void testNotEqualstestImmunizationDto() {
		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
		ImmunizationDto immunizationCareDtoFromFile = mapper.readValue(file, ImmunizationDto.class);
		ImmunizationDto immunizationCareDtoCopy = new ImmunizationDto();
		assertNotEquals(immunizationCareDtoFromFile.equals(immunizationCareDtoCopy), true);
	}

	@Test
	@SneakyThrows
	void testImmunization() {
		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
		Immunization immunizationCareFromFile = mapper.readValue(file, Immunization.class);
		Immunization immunizationCareCopy = new Immunization();

		BeanUtils.copyProperties(immunizationCareFromFile, immunizationCareCopy);
		assertEquals(immunizationCareFromFile, immunizationCareCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsImmunization() {
		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
		Immunization immunizationCareFromFile = mapper.readValue(file, Immunization.class);
		Immunization immunizationCareCopy = new Immunization();
		assertNotEquals(Integer.toString(immunizationCareFromFile.hashCode()),
				Integer.toString(immunizationCareCopy.hashCode()));
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
