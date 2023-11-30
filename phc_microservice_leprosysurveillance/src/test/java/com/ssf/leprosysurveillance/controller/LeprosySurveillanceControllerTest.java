package com.ssf.leprosysurveillance.controller;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.leprosysurveillance.dto.LeprosySurveillanceDto;
import com.ssf.leprosysurveillance.model.LeprosySurveillance;
import com.ssf.leprosysurveillance.service.LeprosySurveillanceService;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@Import(LeprosySurveillance.class)
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class LeprosySurveillanceControllerTest {

	@Autowired
	LeprosySurveillanceService leprosySurveillanceService;

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Autowired
	private MockMvc mockMvc;

	@Test
	@SneakyThrows
	void createLeprosySurveillanceService() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillanceDto leprosySurveillanceDto = mapper.readValue(file, LeprosySurveillanceDto.class);
		try {
			mockMvc.perform(post("/leprosySurveillances").contentType("application/json")
					.content(mapper.writeValueAsString(leprosySurveillanceDto))).andExpect(status().is2xxSuccessful());
		} catch (Exception e) {
			leprosySurveillanceDto.setId(null);
			mockMvc.perform(post("/leprosySurveillances").contentType("application/json")
					.content(mapper.writeValueAsString(leprosySurveillanceDto))).andExpect(status().is2xxSuccessful());
		}

	}

	@Test
	void readLeprosySurveillance() throws Exception {
		mockMvc.perform(get("/leprosySurveillances/string").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void updateLeprosySurveillance() throws Exception {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_patch.json");
		LeprosySurveillanceDto LeprosySurveillancePatchDTO = mapper.readValue(file, LeprosySurveillanceDto.class);
		mockMvc.perform(patch("/leprosySurveillances/string").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(LeprosySurveillancePatchDTO))).andExpect(status().is2xxSuccessful());
	}

}
