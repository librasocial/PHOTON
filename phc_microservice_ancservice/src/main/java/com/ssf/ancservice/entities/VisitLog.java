package com.ssf.ancservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ancservice.constant.ANCServiceConst;
import com.ssf.ancservice.dtos.AuditDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = ANCServiceConst.ANC_VISIT_LOG_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VisitLog extends AuditDto {
    @Id
    private String id;
    private String rchId;
    private String serviceId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = ANCServiceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCServiceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime visitDate;
    private Integer weeksOfPregnancy;
    private Integer weight;
    private Integer midArmCircumference;
    private Integer bpSystolic;
    private Integer bpDiastolic;
    private Integer hb;
    private Boolean isUrineTestDone;
    private Boolean isUrineAlbuminPresent;
    private Boolean isUrineSugarPresent;
    private Boolean isBloodSugarTestDone;
    private Integer fastingSugar;
    private Integer postPrandialSugar;
    private Integer randomSugar;
    private Integer tshLevel;
    private Integer gtt;
    private Integer ogtt;
    private String ttDose;
    private String ttDoseTakenDate;
    private Integer noFATabletsGiven;
    private Integer noIFATabletsGiven;
    private Integer fundalHeightByUterusSizeRatio;
    private Integer foetalHeartRatePerMin;
    private String foetalPosition;
    private String foetalMovements;
    private List<String> highRiskSymptoms;
    private String referralFacilityType;
    private String referralFacility;
    private Boolean isCovidTestDone;
    private Boolean isCovidResultPositive;
    private Boolean isILIExperienced;
    private Boolean didContactCovidPatient;
    private Boolean hasAborted;
    private Boolean hasMaternalDeath;
    private Boolean hasDeliveredChild;
    private String ancFacilityType;
    private String ancFacilityName;
    private String financialYear;
    private String ashaWorker;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    private LocalDate deliverDate;


}
