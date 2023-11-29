package com.ssf.idspsurveillance.entites;

import com.ssf.idspsurveillance.constant.IDSPConstant;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = IDSPConstant.IDSP_COLLECTION_NAME)
public class LooseStools {
    private String suspectedPeriod;
    private Boolean hasDehydration;
    private String extendOfDehydration;
    private Boolean isBloodInStool;
}
