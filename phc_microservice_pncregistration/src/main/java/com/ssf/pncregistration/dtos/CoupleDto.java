package com.ssf.pncregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import com.ssf.pncregistration.validator.UniqueHusband;
import com.ssf.pncregistration.validator.UniqueWife;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoupleDto {
    @UniqueHusband
    private String husbandId;
    private String husbandName;
    @UniqueWife
    private String wifeId;
    private String wifeName;
    private Integer ecSerialNumber;
    private String registeredBy;
    private String registeredByName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime registeredOn;
    private String rchId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime lastANCVisitDate;
}
