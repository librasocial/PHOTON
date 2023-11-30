package com.ssf.abdm.model.sendResponse.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.abdm.model.sendResponse.carecontext.CareContexts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patient {
    private List<CareContexts> careContexts = new ArrayList<>();
    private String referenceNumber;
    private String display;
    private List<String> matchedBy;


}
