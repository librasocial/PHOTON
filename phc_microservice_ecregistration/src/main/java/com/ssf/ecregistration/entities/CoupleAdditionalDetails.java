package com.ssf.ecregistration.entities;

import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(EligibleRegistrationConst.COLLECTION_NAME)
public class CoupleAdditionalDetails {
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
}
