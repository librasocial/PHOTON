//package com.ssf.covidsurveillance.service;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Map;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.util.ResourceUtils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ssf.covidsurveillance.dtos.CovidSurveillanceDto;
//import com.ssf.covidsurveillance.exception.InvalidInputException;
//
//import lombok.SneakyThrows;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@DirtiesContext
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
//public class CovidSurveillanceServiceTest {
//
//	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
//			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
//	@Autowired
//	CovidSurveillanceService covidService;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@SpyBean
//	public AuditorAware<String> auditorProvider;
//
//	@SneakyThrows
//	@Test
//	void createCovidSurveillanceService() {
//		try {
//			File file = ResourceUtils.getFile("classpath:covidsurveillance_sample.json");
//			CovidSurveillanceDto covidSurveillanceDto = mapper.readValue(file, CovidSurveillanceDto.class);
//			assertNotNull(covidService.createCovidSurveillance(covidSurveillanceDto));
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	void getCovidSurveillanceService() throws InvalidInputException {
//		assertNotNull(covidService.readCovidSurveillance("string"));
//	}
//
//	@SneakyThrows
//	@Test
//	void updateCovidSurveillanceService() {
//		CovidSurveillanceDto covidSurveillancePatchDto = CovidSurveillanceDto.builder().build();
//		Map<String, Object> dbMap = objectMapper.convertValue(covidSurveillancePatchDto, Map.class);
//		assertNotNull(covidService.updateCovidSurveillance("string", dbMap));
//	}
//}
