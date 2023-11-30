package com.ssf.immunization.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.immunization.constant.ImmunizationConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImmunizationDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String citizenId;
    private String uhid;
    private String patientId;
    private String encounterId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = ImmunizationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime encounterDate;
    private String recommendationId;
    private String status;
    private String locationOrgId;
    private String vaccineCode;
    private String manufacturer;
    private String batchNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ImmunizationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationConst.ISO_DATE_FORMAT)
    private LocalDate expirationDate;
    private String administeredBy;
    private String administeredByName;
    private ReactionDto reaction;
    private Boolean isCovidTestDone;
    private Boolean isCovidResultPositive;
    private Boolean isILIExperienced;
    private Boolean didContactCovidPatient;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
