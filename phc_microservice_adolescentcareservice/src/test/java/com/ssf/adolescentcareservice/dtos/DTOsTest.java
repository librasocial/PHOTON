package com.ssf.adolescentcareservice.dtos;

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
import com.ssf.adolescentcareservice.entities.AdolescentCareService;

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
	void testAdolescentCareServiceDto() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareServiceDto adolescentCareServiceDtoFromFile = mapper.readValue(file,
				AdolescentCareServiceDto.class);
		AdolescentCareServiceDto adolescentCareServiceDto = AdolescentCareServiceDto.builder().id("random-uuid")
				.childCitizenId("string").visitDate(LocalDateTime.now()).isReferredToPhc(false).servicedBy("string")
				.servicedByName(adolescentCareServiceDtoFromFile.getServicedByName()).commoditiesProvided(null)
				.informationCollected(null).councelingData(null).audit(adolescentCareServiceDtoFromFile.getAudit())
				.build();
		AdolescentCareServiceDto adolescentCareServiceDtoCopy = new AdolescentCareServiceDto();
		BeanUtils.copyProperties(adolescentCareServiceDto, adolescentCareServiceDtoCopy);
		assertEquals(adolescentCareServiceDto, adolescentCareServiceDtoCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsHascodeAdolescentCareServiceDto() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareServiceDto adolescentCareServiceFromFile = mapper.readValue(file, AdolescentCareServiceDto.class);
		AdolescentCareServiceDto adolescentCareServiceDtoCopy = new AdolescentCareServiceDto();
		assertNotEquals(Integer.toString(adolescentCareServiceFromFile.hashCode()),
				Integer.toString(adolescentCareServiceDtoCopy.hashCode()));
	}

	@Test
	@SneakyThrows
	void testNotEqualsAdolescentCareServiceDto() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareServiceDto adolescentCareServiceFromFile = mapper.readValue(file, AdolescentCareServiceDto.class);
		AdolescentCareServiceDto adolescentCareServiceDtoCopy = new AdolescentCareServiceDto();
		assertNotEquals(adolescentCareServiceFromFile.equals(adolescentCareServiceDtoCopy), true);
	}

	@Test
	@SneakyThrows
	void testAdolescentCareService() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareService adolescentCareService = mapper.readValue(file, AdolescentCareService.class);
		adolescentCareService.setServicedByName(adolescentCareService.toString());
		AdolescentCareService adolescentCareServiceCopy = new AdolescentCareService();

		BeanUtils.copyProperties(adolescentCareService, adolescentCareServiceCopy);
		assertEquals(adolescentCareService, adolescentCareServiceCopy);
	}

	@Test
	@SneakyThrows
	void testNotEqualsAdolescentCare() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareService adolescentCareServiceFromFile = mapper.readValue(file, AdolescentCareService.class);
		AdolescentCareService adolescentCareServiceCopy = new AdolescentCareService();
		assertNotEquals(Integer.toString(adolescentCareServiceFromFile.hashCode()),
				Integer.toString(adolescentCareServiceCopy.hashCode()));
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
