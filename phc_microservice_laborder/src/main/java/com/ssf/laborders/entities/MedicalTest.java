package com.ssf.laborders.entities;

import lombok.Data;

@Data
public class MedicalTest {

    private String labTestId;

    private String labTestName;

    private String resultType;

    private String sampleType;
    private String sampleSnomedCode;
}
