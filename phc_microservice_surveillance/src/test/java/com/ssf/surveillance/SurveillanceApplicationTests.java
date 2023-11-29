package com.ssf.surveillance;

import com.ssf.surveillance.controller.LarvaController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SurveillanceApplicationTests {

    @Autowired
    private LarvaController larvaController;

    @Test
    void contextLoads() {
        assertNotNull(larvaController);
    }

    @Test
    void whenPropertyWrong_contextShouldnotLoad() {
        Exception exception = assertThrows(BeanCreationException.class, () -> {
            SurveillanceApplication.main(new String[]{"--spring.profiles.active=incorrect"});
        });
        String expectedMessage = "Error creating bean with name";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
