package com.ssf.bssurveillance.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SampleDto {
    private String slideNo;
    private String isSampleCollected;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime sampleCollectedDate;
    private String feverStartDuration;
    private String numberOf4AQProvided;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime sampleSubmittedDate;
    private String sampleType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime labreceivedDate;
    private String rdtResult;
}
