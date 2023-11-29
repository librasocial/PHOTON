package com.ssf.immunization.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.immunization.constant.ImmunizationConst;
import com.ssf.immunization.dtos.AuditDto;
import com.ssf.immunization.dtos.ReactionDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = ImmunizationConst.IMMUN_COLLECTION_NAME)
public class Immunization extends AuditDto {
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
}
