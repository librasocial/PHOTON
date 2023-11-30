package com.ssf.leprosysurveillance.service;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssf.leprosysurveillance.dto.LeprosySurveillanceDto;
import com.ssf.leprosysurveillance.exception.InvalidInputException;

import lombok.SneakyThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class LeprosySurveillanceServiceTest {

	ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule())
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

	@Autowired
	LeprosySurveillanceService leprosySurveillanceService;

	@Autowired
	private ObjectMapper objectMapper;

	@SpyBean
	public AuditorAware<String> auditorProvider;

	@SneakyThrows
	@Test
	void createLeprosySurveillanceService() {
		File file = ResourceUtils.getFile("classpath:leprosysurveillance_sample.json");
		LeprosySurveillanceDto leprosySurveillanceDto = mapper.readValue(file, LeprosySurveillanceDto.class);
		try {
			assertNotNull(leprosySurveillanceService.createLeprosySurveillance(leprosySurveillanceDto));
		} catch (Exception e) {
			leprosySurveillanceDto.setId(null);
			assertNotNull(leprosySurveillanceService.createLeprosySurveillance(leprosySurveillanceDto));
		}

	}

	@Test
	void getLeprosySurveillanceService() throws InvalidInputException {
		assertNotNull(leprosySurveillanceService.readLeprosySurveillance("string"));
	}

	@SneakyThrows
	@Test
	void updateLeprosySurveillanceService() {
		LeprosySurveillanceDto leprosySurveillancePatchDto = LeprosySurveillanceDto.builder().build();
		Map<String, Object> dbMap = objectMapper.convertValue(leprosySurveillancePatchDto, Map.class);
		assertNotNull(leprosySurveillanceService.updateLeprosySurveillance("string", dbMap));
	}
}
