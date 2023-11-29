package com.ssf.pncservice;

import com.ssf.pncservice.controller.PNCServiceController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
class PNCServiceApplicationTest {

    @Autowired
    private PNCServiceController pncServiceController;

    @Test
    void contextLoads() {
        Assertions.assertThat(pncServiceController).isNotNull();
    }

}
