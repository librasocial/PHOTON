package com.ssf.pncregistration.entities;

import com.ssf.pncregistration.constant.PNCRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = PNCRegistrationConst.PNC_REG_COLLECTION_NAME)
public class MensuralPeriod {
    private LocalDate lmpDate;
    private LocalDate eddDate;

}
