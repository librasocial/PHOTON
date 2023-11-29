package com.ssf.pncregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfantDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private Integer infantLiteral;
    private String name;
    private String pncRegistrationId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    private LocalDate registrationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    private LocalDate dateOfBirth;
    private String financialYear;
    private String infantTerm;
    private String gender;
    private String childId;
    private Integer birthWeight;
    private String defectAtBirth;
    private String  didBabyCryAtBirth;
    private Boolean didBreastFeedingStartWithin1Hr;
    private String wasResuscitationDone;
    private String isReferredToHigherFacility;
    private ImmunizationDto immunization;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
