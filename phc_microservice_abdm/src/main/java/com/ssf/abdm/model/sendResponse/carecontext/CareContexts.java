package com.ssf.abdm.model.sendResponse.carecontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CareContexts {
    private String referenceNumber;
    private String display;
}
