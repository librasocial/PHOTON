package com.ssf.immunizationrec.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.immunizationrec.constant.ImmunizationRecConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VaccinesDto {
    private String code;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    private LocalDate earliestDueDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    private LocalDate vaccinatedDate;
}
