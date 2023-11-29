package com.ssf.pncservice.entities;

import com.ssf.pncservice.constant.PNCServiceConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = PNCServiceConst.PNC_SERVICE_COLLECTION_NAME)
public class Couple {
    private String wifeId;
    private String husbandId;
}
