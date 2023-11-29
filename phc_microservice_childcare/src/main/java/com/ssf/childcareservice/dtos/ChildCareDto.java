package com.ssf.childcareservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.childcareservice.constant.ChildCareServiceConst;
import com.ssf.childcareservice.constant.DataEntryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChildCareDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
    private DataEntryStatusEnum dataEntryStatus;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;

}
