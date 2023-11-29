package com.ssf.laborders.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalTestDTO {
    private String labTestId;
    private String labTestName;
    private String resultType;
    private String sampleType;
    private String sampleSnomedCode;

}
