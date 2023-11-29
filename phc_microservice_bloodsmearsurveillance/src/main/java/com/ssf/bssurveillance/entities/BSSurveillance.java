package com.ssf.bssurveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import com.ssf.bssurveillance.dtos.AuditDto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = BSSurveillanceConst.BSS_COLLECTION_NAME)
public class BSSurveillance extends  AuditDto {
    @Id
    private String id;
    private String citizenId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateOfSurveillance;
    private String permanentAddress;
    private String type;
    private Boolean isReferredToPhc;
    private Sample sample;
    private LabResult labResult;
}
