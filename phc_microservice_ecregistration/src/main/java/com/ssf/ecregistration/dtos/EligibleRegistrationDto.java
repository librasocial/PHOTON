package com.ssf.ecregistration.dtos;

import com.ssf.ecregistration.constant.DataEntryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class EligibleRegistrationDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Valid
    private CoupleDto couple;
    private CoupleAdditionalDetailsDto coupleAdditionalDetails;
    private RchGenerationDto rchGeneration;
    private DataEntryStatusEnum dataEntryStatus;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
