package com.ssf.ancregistration.entities;

import com.ssf.ancregistration.constant.ANCRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = ANCRegistrationConst.ANC_REG_COLLECTION_NAME)
public class Pregnancy {
    public Integer pregnancyNo;
    public List<String> complication;
    public String outcome;
}
