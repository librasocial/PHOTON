package com.ssf.ancregistration;

import com.ssf.ancregistration.controller.ANCRegistrationController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
class ANCRegistrationApplicationTests {

    @Autowired
    private ANCRegistrationController ancRegistrationController;

    @Test
    void contextLoads() {
        Assertions.assertThat(ancRegistrationController).isNotNull();
    }

}
