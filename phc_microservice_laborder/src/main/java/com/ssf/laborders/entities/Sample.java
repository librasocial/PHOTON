package com.ssf.laborders.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.laborders.constants.Constant;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Sample {

    private String labTestId;

    private String labTestName;

    private String resultType;

    private String containerType;

    private Integer containerCount;

    private String sampleType;
    private String sampleSnomedCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime serviceDate;

    private Boolean isOutsourced;

    private Boolean isSampleCollected;

}
