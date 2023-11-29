package com.ssf.primaryhealthcare.dtos.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.primaryhealthcare.utils.PHCConstants;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class DiagnosedWithDTO {

    private String uuid;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PHCConstants.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PHCConstants.ISO_DATE_TIME_FORMAT)
    private LocalDateTime diagnosedOn;

    @CreatedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PHCConstants.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PHCConstants.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;

    private String createdBy;

    @LastModifiedDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PHCConstants.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PHCConstants.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateModified;

    private String modifiedBy;

}
