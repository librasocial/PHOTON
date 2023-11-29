package com.ssf.pncservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import com.ssf.pncservice.dtos.AuditDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = PNCServiceConst.PNC_VISIT_LOG_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VisitLog extends  AuditDto {
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
    @Field(targetType = FieldType.DECIMAL128)
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
}
