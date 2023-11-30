package com.ssf.ancregistration.entities;

import com.ssf.ancregistration.constant.ANCRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = ANCRegistrationConst.ANC_REG_COLLECTION_NAME)
public class PregnantWoman {
    public Integer weight;
    public Integer height;
    public String bloodGroup;
}
