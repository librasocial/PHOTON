package com.ssf.baseprogram.entities;

import com.ssf.baseprogram.constant.BaseProgramConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = BaseProgramConst.BASE_PROGRAM_COLLECTION_NAME)
public class Recipient {
    private String citizenId;
    private Integer age;
    private String hhHeadName;
    private Boolean isCommiteeMember;
}
