package com.ssf.adolescentcareservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.adolescentcareservice.dtos.AdolescentCareServiceDto;
import com.ssf.adolescentcareservice.entities.AdolescentCareService;
import com.ssf.adolescentcareservice.service.AdolescentCareServiceService;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AdolescentCareService.class)
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@EmbeddedKafka
public class AdolescentCareServiceControllerTest {

	@Autowired
	AdolescentCareServiceService adolescentCareService;

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Autowired
	private MockMvc mockMvc;

	@Test
	@SneakyThrows
	void createAdolescentCareService() {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_sample.json");
		AdolescentCareServiceDto adolescentCareServiceSampleDto = mapper.readValue(file,
				AdolescentCareServiceDto.class);
		try {
			mockMvc.perform(post("/adolescentcareservice").contentType("application/json")
					.content(mapper.writeValueAsString(adolescentCareServiceSampleDto)))
					.andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			adolescentCareServiceSampleDto.setId(null);
			mockMvc.perform(post("/adolescentcareservice").contentType("application/json")
					.content(mapper.writeValueAsString(adolescentCareServiceSampleDto)))
					.andExpect(status().is2xxSuccessful());
		}
	}

	@Test
	void readAdolescentCareService() throws Exception {
		mockMvc.perform(get("/adolescentcareservice/string").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void updateAdolescentCareService() throws Exception {
		File file = ResourceUtils.getFile("classpath:adolescentcareservice_patch.json");
		AdolescentCareServiceDto adolescentCareServicePatchDTO = mapper.readValue(file, AdolescentCareServiceDto.class);
		mockMvc.perform(patch("/adolescentcareservice/string").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(adolescentCareServicePatchDTO)))
				.andExpect(status().is2xxSuccessful());
	}
}
