package com.ssf.idspsurveillance.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class FeverDto {
    private String suspectedPeriod;
    private String additionalSymptoms;
}
