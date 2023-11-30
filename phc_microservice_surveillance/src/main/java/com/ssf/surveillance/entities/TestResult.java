package com.ssf.surveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.surveillance.constants.Constant;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TestResult {
    private List<String> reportImageUrl;

    private String labTechName;

    private String h2sResult;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate resultDate;
}
