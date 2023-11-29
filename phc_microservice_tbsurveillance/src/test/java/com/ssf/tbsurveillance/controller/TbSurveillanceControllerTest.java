//package com.ssf.tbsurveillance.controller;
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
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.util.ResourceUtils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ssf.tbsurveillance.dtos.TbSurveillanceDto;
//import com.ssf.tbsurveillance.model.TbSurveillance;
//import com.ssf.tbsurveillance.service.TbSurveillanceService;
//
//import lombok.SneakyThrows;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(TbSurveillance.class)
//@ActiveProfiles("test")
//@DirtiesContext
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
//public class TbSurveillanceControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	TbSurveillanceService tbService;
//
//	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
//			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
//
//	@Test
//	@SneakyThrows
//	void createTbSurveillance() {
//		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
//		TbSurveillanceDto tbSurveillanceDTO = mapper.readValue(file, TbSurveillanceDto.class);
//		try {
//			mockMvc.perform(post("/tbsurveillance").contentType("application/json")
//					.content(mapper.writeValueAsString(tbSurveillanceDTO))).andExpect(status().is2xxSuccessful());
//		} catch (Exception e) {
//			tbSurveillanceDTO.setId(null);
//			mockMvc.perform(post("/tbsurveillance").contentType("application/json")
//					.content(mapper.writeValueAsString(tbSurveillanceDTO))).andExpect(status().is2xxSuccessful());
//		}
//	}
//
//	@Test
//	void getTbSurveillance() throws Exception {
//		mockMvc.perform(get("/tbsurveillance/string").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	void updateUrineSampleSurveillance() throws Exception {
//		File file = ResourceUtils.getFile("classpath:tbsurveillance_patch.json");
//		TbSurveillanceDto tbSurveillancePatchDTO = mapper.readValue(file, TbSurveillanceDto.class);
//		mockMvc.perform(patch("/tbsurveillance/string").contentType(MediaType.APPLICATION_JSON)
//				.content(mapper.writeValueAsString(tbSurveillancePatchDTO))).andExpect(status().is2xxSuccessful());
//	}
//}
