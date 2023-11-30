package com.ssf.surveycube.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssf.surveycube.constant.SurveyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Context {
    private SurveyType surveyType;
    private String household;
    private String village;
    private String citizen;
    @JsonInclude(Include.ALWAYS)
    private Double latitude;
    @JsonInclude(Include.ALWAYS)
    private Double longitude;
    private String displayId;
    private String displayName;
    private Integer totalMembers;
    private Integer totalHouse;
    private Integer totalPopulation;
}
