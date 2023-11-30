package com.ssf.ancregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ancregistration.constant.ANCRegistrationConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MensuralPeriodDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    public LocalDate lmpDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCRegistrationConst.ISO_DATE_FORMAT)
    public LocalDate eddDate;
    public Boolean isRegWithin12w;
    public Boolean isReferredToPHC;
}
