package com.ssf.ecregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoupleAdditionalDetailsDto {
    private String rationCardNo;
    private String husbandAadharEnrollmentNo;
    private String husbandAadharNo;
    private boolean husbandBankAadharLinked;
    private String husbandBankName;
    private String husbandBankBranch;
    private String husbandBankACNo;
    private String husbandBankIFSCCode;
    private String wifeAadharEnrollmentNo;
    private String wifeAadharNo;
    private boolean wifeBankAadharLinked;
    private String wifeBankName;
    private String wifeBankBranch;
    private String wifeBankACNo;
    private String wifeBankIFSCCode;
    private String addedBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime addedOn;
}
