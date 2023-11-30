package com.ssf.adolescentcare.controller;

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
import com.ssf.adolescentcare.dtos.AdolescentCareDto;
import com.ssf.adolescentcare.entities.AdolescentCare;
import com.ssf.adolescentcare.service.AdolescentCareService;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AdolescentCare.class)
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
@EmbeddedKafka
public class AdolescentCareControllerTest {
	@Autowired
	AdolescentCareService adolescentCareService;

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Autowired
	private MockMvc mockMvc;

	@Test
	@SneakyThrows
	void createAdolescentCare() {
		File file = ResourceUtils.getFile("classpath:adolescentcare_sample.json");
		AdolescentCareDto adolescentCareSampleDto = mapper.readValue(file, AdolescentCareDto.class);
		try {
			mockMvc.perform(post("/adolescentcare").contentType("application/json")
					.content(mapper.writeValueAsString(adolescentCareSampleDto))).andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			adolescentCareSampleDto.setId(null);
			mockMvc.perform(post("/adolescentcare").contentType("application/json")
					.content(mapper.writeValueAsString(adolescentCareSampleDto))).andExpect(status().is2xxSuccessful());
		}
	}

	@Test
	void readAdolescentCare() throws Exception {
		mockMvc.perform(get("/adolescentcare/string").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void updateAdolescentCare() throws Exception {
		File file = ResourceUtils.getFile("classpath:adolescentcare_patch.json");
		AdolescentCareDto adolescentCareDtoPatchDTO = mapper.readValue(file, AdolescentCareDto.class);
		mockMvc.perform(patch("/adolescentcare/string").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(adolescentCareDtoPatchDTO))).andExpect(status().is2xxSuccessful());
	}

}
