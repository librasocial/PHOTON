package com.ssf.surveillance.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LarvaFilterDTO {
    private Integer page;
    private Integer size;
    private Optional<String> dateOfSurvey;
    private Optional<String> placeType;
    private Optional<String> villageId;
    private Optional<String> placeOrgId;
    private Optional<String> householdId;
}
