package com.ssf.immunizationrec.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RecommendationsDto {
    private List<VaccinesDto> vaccines;
    private String targetDiseaseName;
    private String description;
    private String series;
    private String doseNumber;
}
