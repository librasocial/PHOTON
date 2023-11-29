package com.ssf.immunizationrec.entities;

import com.ssf.immunizationrec.constant.ImmunizationRecConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = ImmunizationRecConst.IMMUN_REC_COLLECTION_NAME)
public class Recommendations {
    private List<Vaccines> vaccines;
    private String targetDiseaseName;
    private String description;
    private String series;
    private String doseNumber;
}
