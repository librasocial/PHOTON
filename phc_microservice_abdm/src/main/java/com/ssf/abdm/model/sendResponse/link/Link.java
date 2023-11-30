package com.ssf.abdm.model.sendResponse.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Link {
    private String referenceNumber;
    private String authenticationType;
    private Meta meta;


}
