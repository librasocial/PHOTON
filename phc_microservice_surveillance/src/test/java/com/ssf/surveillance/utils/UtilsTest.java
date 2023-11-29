package com.ssf.surveillance.utils;

import com.ssf.surveillance.exception.SurveillanceException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
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
        assertThrows(SurveillanceException.class, () -> {
            Utils.stringToLocalDate("2022-07-1000000");
        });
    }

    @Test
    void testStringToLocalDateTime_WrongInput() {
        assertThrows(SurveillanceException.class, () -> {
            Utils.stringToLocalDateTime("2022-07-1000000");
        });
    }
}