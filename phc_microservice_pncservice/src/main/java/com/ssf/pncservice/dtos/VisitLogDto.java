package com.ssf.pncservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VisitLogDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String rchId;
    private String serviceId;
    private Integer pncPeriod;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PNCServiceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime pncDate;
    private Integer noIFATabletsGiven;
    private String ppcMethod;
    private String signOfDangerToTheMother;
    private String referralFacility;
    private String referralPlace;
    private String remark;
    private Boolean isCovidTestDone;
    private Boolean isCovidResultPositive;
    private Boolean isUrineTestDone;
    private Boolean isILIExperienced;
    private Boolean didContactCovidPatient;
    private String complaints;
    private String pallor;
    private String bloodPressure;
    private BigDecimal temperature;
    private String breastsCondition;
    private String nippleCondition;
    private String uterusTenderness;
    private String bleeding;
    private String lochiaCondition;
    private String episiotomyORTear;
    private String familyPlanCounselling;
    private String complications;
    private Boolean isReferredToPHC;
    private Boolean hasMaternalDeath;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
