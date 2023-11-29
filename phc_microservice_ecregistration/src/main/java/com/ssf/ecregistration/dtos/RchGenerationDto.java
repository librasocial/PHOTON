package com.ssf.ecregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RchGenerationDto {
    private String rchId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime generatedDateTime;
}
