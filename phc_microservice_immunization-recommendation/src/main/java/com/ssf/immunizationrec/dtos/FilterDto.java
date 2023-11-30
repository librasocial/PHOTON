package com.ssf.immunizationrec.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssf.immunizationrec.constant.ImmunizationRecConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
public class FilterDto {
    private Integer pageNumber;
    private Integer pageSize;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    private LocalDate recommendedDate;
    private String citizenId;
    private String patientId;
    private String uhid;
}
