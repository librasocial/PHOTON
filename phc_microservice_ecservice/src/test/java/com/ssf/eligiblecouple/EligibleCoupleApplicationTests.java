package com.ssf.eligiblecouple;

import com.ssf.eligiblecouple.controller.EligibleCoupleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@ActiveProfiles("test")
class EligibleCoupleApplicationTests {

    @Autowired
    private EligibleCoupleController eligibleCoupleController;

    @Test
    void contextLoads() {
        assertThat(eligibleCoupleController).isNotNull();
    }

    @Test
    public void whenPropertyWrong_contextShouldnotLoad() {
        Exception exception = assertThrows(BeanCreationException.class, () -> {
            EligibleCoupleApplication.main(new String[]{"--spring.profiles.active=incorrect"});
        });
        String expectedMessage = "Error creating bean with name";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
