package com.ssf.eligiblecouple.dtos;

import com.ssf.eligiblecouple.validator.UniqueHusband;
import com.ssf.eligiblecouple.validator.UniqueWife;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoupleDto {
    @NotNull(message = "wifeId should not be null")
    @UniqueWife
    private String wifeId;
    @NotNull(message = "husbandId should not be null")
    @UniqueHusband
    private String husbandId;
}
