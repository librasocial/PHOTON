package com.ssf.pncservice.dtos;

import com.ssf.pncservice.validator.UniqueHusband;
import com.ssf.pncservice.validator.UniqueWife;
import lombok.*;

import javax.validation.constraints.NotNull;

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
