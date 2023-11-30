//package com.ssf.childcareimmunization.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.File;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.util.ResourceUtils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ssf.childcareimmunization.dtos.ImmunizationDto;
//import com.ssf.childcareimmunization.entities.Immunization;
//
//import lombok.SneakyThrows;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(Immunization.class)
//@ActiveProfiles("test")
////@DirtiesContext
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
////@EmbeddedKafka
//public class ImmunizationControllerTest {
//
//	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
//			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Test
//	@SneakyThrows
//	void createImmunization() {
//		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
//		ImmunizationDto immunizationCareSampleDto = mapper.readValue(file, ImmunizationDto.class);
//		try {
//			mockMvc.perform(post("/immunizations").contentType("application/json")
//					.content(mapper.writeValueAsString(immunizationCareSampleDto)))
//					.andExpect(status().is2xxSuccessful());
//		} catch (Exception e) {
//			immunizationCareSampleDto.setId(null);
//			mockMvc.perform(post("/immunizations").contentType("application/json")
//					.content(mapper.writeValueAsString(immunizationCareSampleDto)))
//					.andExpect(status().is2xxSuccessful());
//		}
//	}
//
//	@Test
//	void readImmunization() throws Exception {
//		mockMvc.perform(get("/immunizations/string").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	void updateImmunization() throws Exception {
//		File file = ResourceUtils.getFile("classpath:immunizationcare_patch.json");
//		ImmunizationDto immunizationCarePatchDTO = mapper.readValue(file, ImmunizationDto.class);
//		mockMvc.perform(patch("/immunizations/string").contentType(MediaType.APPLICATION_JSON)
//				.content(mapper.writeValueAsString(immunizationCarePatchDTO))).andExpect(status().is2xxSuccessful());
//	}
//
//}
