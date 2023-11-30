package com.ssf.pncservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfantVisitLogDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String childId;
    private String serviceId;
    private Integer pncPeriod;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    private LocalDate pncDate;
    private String signOfDangerToTheInfant;
    private String referralFacility;
    private String referralPlace;
    private String remark;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
