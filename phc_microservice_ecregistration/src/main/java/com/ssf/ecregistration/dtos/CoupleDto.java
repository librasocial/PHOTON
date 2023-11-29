package com.ssf.ecregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import com.ssf.ecregistration.validator.UniqueHusband;
import com.ssf.ecregistration.validator.UniqueWife;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoupleDto {
    @UniqueHusband
    private String husbandId;
    private String husbandName;
    private String husbandPhone;
    private Integer husbandAge;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = EligibleRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleRegistrationConst.ISO_DATE_FORMAT)
    private LocalDate husbandDOB;
    private String husbandHealthId;
    private Integer husbandAgeAtMarriage;
    @UniqueWife
    private String wifeId;
    private String wifeName;
    private String wifePhone;
    private Integer wifeAge;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = EligibleRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleRegistrationConst.ISO_DATE_FORMAT)
    private LocalDate wifeDOB;
    private String wifeHealthId;
    private Integer wifeAgeAtMarriage;
    private String religion;
    private String caste;
    private String economicStatus;
    private Integer totalMaleChildren;
    private Integer totalFemaleChildren;
    private Integer totalMaleChildrenAlive;
    private Integer totalFemaleChildrenAlive;
    private String infertilityStatus;
    private Integer serialNumber;
    private String registeredBy;
    private String registeredByName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime registeredOn;
}
