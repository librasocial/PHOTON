package com.ssf.ancregistration.entities;

import com.ssf.ancregistration.constant.ANCRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = ANCRegistrationConst.ANC_REG_COLLECTION_NAME)
public class MensuralPeriod {
    public LocalDate lmpDate;
    public LocalDate eddDate;
    public Boolean isRegWithin12w;
    public Boolean isReferredToPHC;
}
