package com.ssf.abdm.model.sendResponse.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meta {
    private String communicationMedium;
    private String communicationHint;
    private String communicationExpiry;

}
