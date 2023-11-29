package com.ssf.bssurveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = BSSurveillanceConst.BSS_COLLECTION_NAME)
public class Sample {
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
