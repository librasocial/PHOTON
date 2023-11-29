package com.ssf.idspsurveillance.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OthersDto {
    private String otherSymptom;
    private String additionalSymptoms;
}
