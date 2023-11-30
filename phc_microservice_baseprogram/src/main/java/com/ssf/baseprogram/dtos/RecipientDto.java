package com.ssf.baseprogram.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RecipientDto {
    private String citizenId;
    private Integer age;
    private String hhHeadName;
    private Boolean isCommiteeMember;
}
