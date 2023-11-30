package com.ssf.surveycube.dtos;

import com.ssf.surveycube.constant.SurveyType;
import com.ssf.surveycube.constant.Topics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterDto {
    private Topics topics;
    private SurveyType surveyType;
    private MultiValueMap<String, String> filters;
    private List<String> citizenId;
    private List<String> houseHoldId;
    private List<String> villageId;
}
