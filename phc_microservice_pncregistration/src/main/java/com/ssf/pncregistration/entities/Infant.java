package com.ssf.pncregistration.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import com.ssf.pncregistration.dtos.AuditDto;;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = PNCRegistrationConst.PNC_INFANT_COLLECTION_NAME)
public class Infant extends AuditDto  {
    @Id
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
    private String didBabyCryAtBirth;
    private Boolean didBreastFeedingStartWithin1Hr;
    private String wasResuscitationDone;
    private String isReferredToHigherFacility;
    private Immunization immunization;
}
