package com.ssf.eligiblecouple.entities;

import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = EligibleCoupleConst.ELIGIBLE_COUPLE_COLLECTION_NAME)
public class Couple {
    private String wifeId;
    private String husbandId;
}
