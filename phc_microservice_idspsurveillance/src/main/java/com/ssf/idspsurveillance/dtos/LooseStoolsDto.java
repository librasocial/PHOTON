package com.ssf.idspsurveillance.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LooseStoolsDto {
    private String suspectedPeriod;
    private Boolean hasDehydration;
    private String extendOfDehydration;
    private Boolean isBloodInStool;
}
