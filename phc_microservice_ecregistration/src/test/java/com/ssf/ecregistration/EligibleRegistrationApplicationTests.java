package com.ssf.ecregistration;

import com.ssf.ecregistration.controller.EligibleRegistrationController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
class EligibleRegistrationApplicationTests {

    @Autowired
    private EligibleRegistrationController eligibleRegistrationController;

    @Test
    void contextLoads() {
        Assertions.assertThat(eligibleRegistrationController).isNotNull();
    }

    @Test
    public void whenPropertyWrong_contextShouldnotLoad() {
        Exception exception = assertThrows(BeanCreationException.class, () -> {
            EligibleRegistrationApplication.main(new String[]{"--spring.profiles.active=incorrect"});
        });
        String expectedMessage = "Error creating bean with name";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
