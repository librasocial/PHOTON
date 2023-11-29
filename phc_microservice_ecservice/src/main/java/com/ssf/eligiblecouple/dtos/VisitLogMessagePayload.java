package com.ssf.eligiblecouple.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitLogMessagePayload {
    private VisitLogDto visitLogDto;
    private EligibleCoupleDto eligibleCoupleDto;
}
