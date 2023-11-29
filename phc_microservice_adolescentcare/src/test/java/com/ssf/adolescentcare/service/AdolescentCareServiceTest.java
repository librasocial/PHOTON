package com.ssf.adolescentcare.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.adolescentcare.dtos.AdolescentCareDto;
import com.ssf.adolescentcare.exception.InvalidInputException;

import lombok.SneakyThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@EmbeddedKafka
public class AdolescentCareServiceTest {

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Autowired
	AdolescentCareService adolescentCareService;

	@Autowired
	private ObjectMapper objectMapper;

	@SpyBean
	public AuditorAware<String> auditorProvider;

	@SneakyThrows
	@Test
	void createAdolescentCareService() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCareDto adolescentCareSampleDto = mapper.readValue(file, AdolescentCareDto.class);
		try {
			assertNotNull(adolescentCareService.createAdolescentCare(adolescentCareSampleDto));
		} catch (Exception e) {
			adolescentCareSampleDto.setId(null);
			assertNotNull(adolescentCareService.createAdolescentCare(adolescentCareSampleDto));
		}
	}

	@Test
	void getAdolescentCareService() throws InvalidInputException {
		assertNotNull(adolescentCareService.getAdolescentCare("string"));
	}

	@SneakyThrows
	@Test
	void updateAdolescentCare() {
		AdolescentCareDto adolescentCarePatchDto = AdolescentCareDto.builder().build();
		Map<String, Object> dbMap = objectMapper.convertValue(adolescentCarePatchDto, Map.class);
		assertNotNull(adolescentCareService.updateAdolescentCare("string", dbMap));
	}
}
