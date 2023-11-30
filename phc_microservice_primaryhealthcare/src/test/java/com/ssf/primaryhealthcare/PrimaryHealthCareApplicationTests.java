package com.ssf.primaryhealthcare;

import com.ssf.primaryhealthcare.controller.PrimaryHealthCareController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class PrimaryHealthCareApplicationTests {
    @Autowired
    private PrimaryHealthCareController primaryHealthCareController;

    @Test
    void contextLoads() {
        Assertions.assertThat(primaryHealthCareController).isNotNull();
    }

}
