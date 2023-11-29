package com.ssf.laborders;

import com.ssf.laborders.controller.LabOrdersController;
import com.ssf.laborders.controller.OrderSamplesController;
import com.ssf.laborders.controller.OrdersSummaryController;
import com.ssf.laborders.controller.TestResultsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class LabOrdersApplicationTests {

    @Autowired
    private LabOrdersController labOrdersController;

    @Autowired
    private OrderSamplesController orderSamplesController;

    @Autowired
    private TestResultsController testResultsController;

    @Autowired
    private OrdersSummaryController ordersSummaryController;

    @Test
    void contextLoads() {
        assertThat(labOrdersController).isNotNull();
        assertThat(orderSamplesController).isNotNull();
        assertThat(testResultsController).isNotNull();
        assertThat(ordersSummaryController).isNotNull();
    }

    @Test
     void whenPropertyWrong_contextShouldnotLoad() {
        Exception exception = assertThrows(BeanCreationException.class, () -> {
            LabOrdersApplication.main(new String[]{"--spring.profiles.active=incorrect"});
        });
        String expectedMessage = "Error creating bean with name";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
