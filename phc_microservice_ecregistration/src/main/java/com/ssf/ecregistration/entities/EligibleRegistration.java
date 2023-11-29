package com.ssf.ecregistration.entities;

import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import com.ssf.ecregistration.dtos.AuditDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(EligibleRegistrationConst.COLLECTION_NAME)
public class EligibleRegistration extends AuditDto {
    @Id
    private String id;
    private Couple couple;
    private CoupleAdditionalDetails coupleAdditionalDetails;
    private String dataEntryStatus;
    private RchGeneration rchGeneration;
}
