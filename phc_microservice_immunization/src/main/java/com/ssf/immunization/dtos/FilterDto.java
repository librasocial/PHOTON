package com.ssf.immunization.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
public class FilterDto {
    private Integer pageNumber;
    private Integer pageSize;
    private String recommendationId;
    private String citizenId;
    private String patientId;
    private String uhid;
}
