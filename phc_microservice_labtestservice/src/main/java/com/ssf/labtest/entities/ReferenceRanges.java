package com.ssf.labtest.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceRanges {
    private String gender;
    private String period;
    private Integer fromAge;
    private Integer toAge;
    private String referenceIndicator;
    private String minValue;
    private String maxValue;
    private String description;
}
