package com.ssf.adolescentcare.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.adolescentcare.entities.AdolescentCare;

import lombok.SneakyThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@EmbeddedKafka
public class DTOsTest {
	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Test
	@SneakyThrows
	void testAdolescentCareDto() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCareDto adolescentCareDtoFromFile = mapper.readValue(file, AdolescentCareDto.class);
		AdolescentCareDto adolescentCareDto = AdolescentCareDto.builder().id("random-uuid").citizenId("string")
				.rchId("string").registeredBy("string")
				.registeredByName(adolescentCareDtoFromFile.getRegisteredByName().toString())
				.registeredOn(LocalDateTime.now()).financialYear(2010).fatherName("string").mobileNumber(null)
				.mobileOwner(null).birthCertificateNo(null).childAadharNo(null).isCovidTestDone(null)
				.isCovidResultPositive(null).isILIExperienced(null).didContactCovidPatient(null)
				.dataEntryStatus(adolescentCareDtoFromFile.getDataEntryStatus())
				.audit(adolescentCareDtoFromFile.getAudit()).build();
		AdolescentCareDto adolescentCareDtoCopy = new AdolescentCareDto();
		BeanUtils.copyProperties(adolescentCareDto, adolescentCareDtoCopy);
		assertEquals(adolescentCareDto, adolescentCareDtoCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsHascodeAdolescentCareDto() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCareDto adolescentCareFromFile = mapper.readValue(file, AdolescentCareDto.class);
		AdolescentCareDto adolescentCareDtoCopy = new AdolescentCareDto();
		assertNotEquals(Integer.toString(adolescentCareFromFile.hashCode()),
				Integer.toString(adolescentCareDtoCopy.hashCode()));
	}

	@Test
	@SneakyThrows
	void testNotEqualsAdolescentCareDto() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCareDto adolescentCareFromFile = mapper.readValue(file, AdolescentCareDto.class);
		AdolescentCareDto adolescentCareDtoCopy = new AdolescentCareDto();
		assertNotEquals(adolescentCareFromFile.equals(adolescentCareDtoCopy), true);
	}

	@Test
	@SneakyThrows
	void testAdolescentCare() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCare adolescentCare = mapper.readValue(file, AdolescentCare.class);
		adolescentCare.setFatherName(adolescentCare.toString());
		AdolescentCare adolescentCareCopy = new AdolescentCare();

		BeanUtils.copyProperties(adolescentCare, adolescentCareCopy);
		assertEquals(adolescentCare, adolescentCareCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsAdolescentCare() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCare adolescentCareFromFile = mapper.readValue(file, AdolescentCare.class);
		AdolescentCare adolescentCareCopy = new AdolescentCare();
		assertNotEquals(Integer.toString(adolescentCareFromFile.hashCode()),
				Integer.toString(adolescentCareCopy.hashCode()));
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
