package com.ssf.pncregistration.dtos;

import com.ssf.pncregistration.constant.DataEntryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PNCRegistrationDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Valid
    private CoupleDto couple;
    private MensuralPeriodDto mensuralPeriod;
    private DeliveryDetailsDto deliveryDetails;
    private Boolean isCovidTestDone;
    private Boolean isCovidResultPositive;
    private Boolean isILIExperienced;
    private Boolean didContactCovidPatient;
    private DataEntryStatusEnum dataEntryStatus;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
