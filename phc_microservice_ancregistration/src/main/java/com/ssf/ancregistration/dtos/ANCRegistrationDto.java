package com.ssf.ancregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ancregistration.constant.ANCRegistrationConst;
import com.ssf.ancregistration.constant.DataEntryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ANCRegistrationDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public String id;
    @Valid
    public CoupleDto couple;
    public MensuralPeriodDto mensuralPeriod;
    public PregnantWomanDto pregnantWoman;
    public List<String> medicalHistory;
    public Integer countPregnancies;
    public List<PregnancyDto> pregnancies;
    public String expectedDeliveryFacilityType;
    public String expectedDeliveryFacility;
    public Boolean isVDRLTestCompleted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    public LocalDate vdrlTestDate;
    public String vdrlTestResult;
    public Boolean isHIVTestCompleted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    public LocalDate hivTestDate;
    public String hivTestResult;
    public Boolean isHBsAGTestCompleted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    public LocalDate hbsagTestDate;
    public String hbsagTestResult;
    public DataEntryStatusEnum dataEntryStatus;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
