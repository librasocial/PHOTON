package com.ssf.ecregistration.entities;

import com.ssf.ecregistration.constant.EligibleRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(EligibleRegistrationConst.COLLECTION_NAME)
public class RchGeneration {
    private String rchId;
    private LocalDateTime generatedDateTime;
}
