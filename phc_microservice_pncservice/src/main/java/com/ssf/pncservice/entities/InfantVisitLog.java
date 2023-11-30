package com.ssf.pncservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import com.ssf.pncservice.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = PNCServiceConst.PNC_INFANT_VISIT_LOG_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfantVisitLog extends  AuditDto  {
    private String id;
    private String childId;
    private String serviceId;
    private Integer pncPeriod;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PNCServiceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime pncDate;
    private String signOfDangerToTheInfant;
    private String referralFacility;
    private String referralPlace;
    private String remark;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal weight;
    private String urinePassed;
    private String stoolPassed;
    private BigDecimal temperature;
    private Boolean isILIExperienced;
    private String jaundice;
    private String diarrhea;
    private String vomiting;
    private String convulsions;
    private String activity;
    private String sucking;
    private String breathing;
    private String chestDrawing;
    private String skinPustules;
    private String umbilicalStumpCondition;
    private String complications;
    private Boolean hasInfantDeath;
}
