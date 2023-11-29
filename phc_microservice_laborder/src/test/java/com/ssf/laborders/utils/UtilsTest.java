package com.ssf.laborders.utils;

import com.ssf.laborders.exception.LabOrdersException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class UtilsTest {

    @Test
    void testStringToLocalDateTime() {
        assertNotNull(Utils.stringToLocalDateTime("2022-07-10T00:00:00.000Z"));
    }

    @Test
    void testStringToLocalDate() {
        assertNotNull(Utils.stringToLocalDate("2022-07-10"));
    }

    @Test
    void testStringToLocalDate_WrongInput() {
        assertThrows(LabOrdersException.class, () -> {
            Utils.stringToLocalDate("2022-07-1000000");
        });
    }

    @Test
    void testStringToLocalDateTime_WrongInput() {
        assertThrows(LabOrdersException.class, () -> {
            Utils.stringToLocalDateTime("2022-07-1000000");
        });
    }
}