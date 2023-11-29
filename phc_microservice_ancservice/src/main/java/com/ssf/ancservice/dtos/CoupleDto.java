package com.ssf.ancservice.dtos;

import com.ssf.ancservice.validator.UniqueHusband;
import com.ssf.ancservice.validator.UniqueWife;
import lombok.*;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoupleDto {
    @UniqueWife
    private String wifeId;
    @UniqueHusband
    private String husbandId;
}
