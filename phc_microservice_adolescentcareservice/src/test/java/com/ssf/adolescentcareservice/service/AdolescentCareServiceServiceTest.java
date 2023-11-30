package com.ssf.adolescentcareservice.service;

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
import com.ssf.adolescentcareservice.dtos.AdolescentCareServiceDto;
import com.ssf.adolescentcareservice.exception.InvalidInputException;

import lombok.SneakyThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@EmbeddedKafka
public class AdolescentCareServiceServiceTest {

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Autowired
	AdolescentCareServiceService adolescentCareService;
	
	@Autowired
	private ObjectMapper objectMapper;

	@SpyBean
	public AuditorAware<String> auditorProvider;

	@SneakyThrows
	@Test
	void createAdolescentCareServiceService() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareServiceDto adolescentCareServiceSampleDto = mapper.readValue(file,
				AdolescentCareServiceDto.class);
		try {
			assertNotNull(adolescentCareService.createAdolescentCareService(adolescentCareServiceSampleDto));
		} catch (Exception e) {
			adolescentCareServiceSampleDto.setId(null);
			assertNotNull(adolescentCareService.createAdolescentCareService(adolescentCareServiceSampleDto));
		}
	}

	@Test
	void getAdolescentCareServiceService() throws InvalidInputException {
		assertNotNull(adolescentCareService.getAdolescentCareService("string"));
	}

	@SneakyThrows
	@Test
	void updateAdolescentCareService() {
		AdolescentCareServiceDto adolescentCareServicePatchDto = AdolescentCareServiceDto.builder().build();
		Map<String, Object> dbMap = objectMapper.convertValue(adolescentCareServicePatchDto, Map.class);
		assertNotNull(adolescentCareService.updateAdolescentCareService("string", dbMap));
	}

}
