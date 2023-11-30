package com.ssf.surveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.surveillance.constants.Constant;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Sample {

    private String noOfSamples;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate collectionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate labSubmittedDate;

}
