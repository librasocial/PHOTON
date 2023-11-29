package com.ssf.surveillance.utils;

import com.ssf.surveillance.constants.Constant;
import com.ssf.surveillance.exception.SurveillanceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    @SneakyThrows
    public static LocalDateTime stringToLocalDateTime(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.ISO_DATE_TIME_FORMAT);
            return LocalDateTime.parse(date, formatter);
        } catch (Exception e) {
            throw new SurveillanceException("Date Format must be : 'yyyy-MM-dd'");
        }
    }

    @SneakyThrows
    public static LocalDate stringToLocalDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.ISO_DATE_FORMAT);
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            throw new SurveillanceException("Date Format must be : 'yyyy-MM-dd'");
        }
    }
}
