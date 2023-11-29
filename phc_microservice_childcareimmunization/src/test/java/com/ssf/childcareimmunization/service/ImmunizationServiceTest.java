//package com.ssf.childcareimmunization.service;
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
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.util.ResourceUtils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.ssf.childcareimmunization.dtos.ImmunizationDto;
//import com.ssf.childcareimmunization.exception.InvalidInputException;
//
//import lombok.SneakyThrows;
//
//@SpringBootTest
//@ActiveProfiles("test")
////@DirtiesContext
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
////@EmbeddedKafka
//public class ImmunizationServiceTest {
//	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
//			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();
//
//	@Autowired
//	ImmunizationService immunizationService;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@SneakyThrows
//	@Test
//	void createImmunization() {
//		File file = ResourceUtils.getFile("classpath:immunizationcare_sample.json");
//		ImmunizationDto immunizationCareSampleDto = mapper.readValue(file, ImmunizationDto.class);
//		try {
//			assertNotNull(immunizationService.createImmunization(immunizationCareSampleDto));
//		} catch (Exception e) {
//			immunizationCareSampleDto.setId(null);
//			assertNotNull(immunizationService.createImmunization(immunizationCareSampleDto));
//		}
//	}
//
//	@Test
//	void readImmunization() throws InvalidInputException {
//		assertNotNull(immunizationService.getImmunization("string"));
//	}
//
//	@SneakyThrows
//	@Test
//	void updateImmunization() {
//		ImmunizationDto immunizationCarePatchDto = ImmunizationDto.builder().build();
//		Map<String, Object> dbMap = objectMapper.convertValue(immunizationCarePatchDto, Map.class);
//		assertNotNull(immunizationService.updateImmunization("string", dbMap));
//	}
//
//}
