package com.ssf.pncregistration.entities;

import com.ssf.pncregistration.constant.PNCRegistrationConst;
import com.ssf.pncregistration.dtos.AuditDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = PNCRegistrationConst.PNC_REG_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PNCRegistration extends AuditDto {
    @Id
    private String id;
    private Couple couple;
    private MensuralPeriod mensuralPeriod;
    private DeliveryDetails deliveryDetails;
    private Boolean isCovidTestDone;
    private Boolean isCovidResultPositive;
    private Boolean isILIExperienced;
    private Boolean didContactCovidPatient;
    private String dataEntryStatus;
}
