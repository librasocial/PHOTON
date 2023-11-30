package com.ssf.immunization.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.immunization.constant.ImmunizationConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReactionDto {
    private String type;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = ImmunizationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime date;
    private String detail;
    private Boolean isCaseClosed;
    private String caseClosureReason;
    private DeathDto death;
}
