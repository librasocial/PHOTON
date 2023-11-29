package com.ssf.childcareimmunization;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
class PhcChildCareImmunizationApplicationTests {

	@Test
	void contextLoads() {
	}

}
