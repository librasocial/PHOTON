package com.ssf.ecregistration.entities;

import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(EligibleRegistrationConst.COLLECTION_NAME)
public class Couple {

    private String husbandId;
    private String husbandName;
    private String husbandPhone;
    private Integer husbandAge;
    private LocalDate husbandDOB;
    private String husbandHealthId;
    private Integer husbandAgeAtMarriage;
    private String wifeId;
    private String wifeName;
    private String wifePhone;
    private Integer wifeAge;
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
    private LocalDateTime registeredOn;
}
