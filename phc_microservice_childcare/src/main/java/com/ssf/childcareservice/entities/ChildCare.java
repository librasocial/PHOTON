package com.ssf.childcareservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.childcareservice.constant.ChildCareServiceConst;
import com.ssf.childcareservice.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = ChildCareServiceConst.CHILD_CARE_SERVICE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChildCare extends  AuditDto {
    @Id
    private String id;
    private String citizenId;
    private String childId;
    private Integer serialNumber;
    private String registeredBy;
    private String registeredByName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = ChildCareServiceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ChildCareServiceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime registeredOn;
    private String financialYear;
    private String fatherName;
    private String mobileNumber;
    private String mobileOwner;
    private String birthCertificateNo;
    private String placeOfBirth;
    private Boolean isBornInOtherDistrict;
    private String aadharEnrollmentNo;
    private String aadharNo;
    private Boolean isCovidTestDone;
    private Boolean isCovidResultPositive;
    private Boolean isILIExperienced;
    private Boolean didContactCovidPatient;
    private String immunizationRecommendationId;
    private String dataEntryStatus;
}
