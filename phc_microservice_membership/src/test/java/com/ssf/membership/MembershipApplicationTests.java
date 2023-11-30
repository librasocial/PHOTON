package com.ssf.membership;

import com.ssf.membership.controller.MemberController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Config;
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
public class MembershipApplicationTests {
    @Autowired
    private MemberController memberController;

    @Test
    void contextLoads() {
        Assertions.assertThat(memberController).isNotNull();
    }

//    @Test
    public void whenPropertyWrong_contextShouldnotLoad() {
        Exception exception = assertThrows(BeanCreationException.class, () -> {
            MembershipApplication.main(new String[]{"--spring.profiles.active=incorrect"});
        });
        String expectedMessage = "Error creating bean with name";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
