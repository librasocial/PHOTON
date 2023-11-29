//package com.ssf.tbsurveillance.service;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.io.File;
//import java.util.Map;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.util.ResourceUtils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ssf.tbsurveillance.dtos.TbSurveillanceDto;
//import com.ssf.tbsurveillance.exception.InvalidInputException;
//
//import lombok.SneakyThrows;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@DirtiesContext
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
//public class TbSurveillanceServiceTest {
//
//	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
//			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
//
//	@Autowired
//	TbSurveillanceService tbService;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@SneakyThrows
//	@Test
//	void createTbSurveillanceService() {
//		File file = ResourceUtils.getFile("classpath:tbsurveillance_sample.json");
//		TbSurveillanceDto tbSurveillanceDtO = mapper.readValue(file, TbSurveillanceDto.class);
//		try {
//			assertNotNull(tbService.createTbSurveillance(tbSurveillanceDtO));
//		} catch (Exception e) {
//			tbSurveillanceDtO.setId(null);
//			assertNotNull(tbService.createTbSurveillance(tbSurveillanceDtO));
//		}
//
//	}
//
//	@Test
//	void getTbSurveillance() throws InvalidInputException {
//		assertNotNull(tbService.getTbSurveillance("string"));
//	}
//
//	@SneakyThrows
//	@Test
//	void updateTbSurveillanceService() {
//		TbSurveillanceDto tbSurveillancePatchDto = TbSurveillanceDto.builder().build();
//		Map<String, Object> dbMap = objectMapper.convertValue(tbSurveillancePatchDto, Map.class);
//		assertNotNull(tbService.updateTbSurveillance("string", dbMap));
//	}
//
//}
