package com.ssf.pncregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MensuralPeriodDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    private LocalDate lmpDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_FORMAT)
    private LocalDate eddDate;
}
