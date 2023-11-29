//package com.ssf.covidsurveillance.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.util.ResourceUtils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ssf.covidsurveillance.dtos.CovidSurveillanceDto;
//import com.ssf.covidsurveillance.entities.CovidSurveillance;
//import com.ssf.covidsurveillance.service.CovidSurveillanceService;
//
//import lombok.SneakyThrows;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(CovidSurveillance.class)
//@ActiveProfiles("test")
//@DirtiesContext
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
//public class CovidSurveillanceControllerTest {
//
//	@Autowired
//	CovidSurveillanceService covidService;
//
//	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
//			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Test
//	@SneakyThrows
//	void createCovidSurveillance() {
//		File file = null;
//		try {
//			file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
//			CovidSurveillanceDto CovidSurveillanceDto = mapper.readValue(file, CovidSurveillanceDto.class);
//			mockMvc.perform(post("/covidsurveillances").contentType("application/json")
//					.content(mapper.writeValueAsString(CovidSurveillanceDto))).andExpect(status().is2xxSuccessful());
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	void readCovidSurveillance() throws Exception {
//		mockMvc.perform(get("/covidsurveillances/string").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	void updateCovidSurveillance() throws Exception {
//		File file = ResourceUtils.getFile("classpath:covidsurveillance_patch.json");
//		CovidSurveillanceDto covidSurveillancePatchDTO = mapper.readValue(file, CovidSurveillanceDto.class);
//		mockMvc.perform(patch("/covidsurveillances/string").contentType(MediaType.APPLICATION_JSON)
//				.content(mapper.writeValueAsString(covidSurveillancePatchDTO))).andExpect(status().is2xxSuccessful());
//	}
//
//}
