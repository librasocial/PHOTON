package com.ssf.product.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UomDto {
    private String alternateUOM;
    private String alternateUOMUnit;
    private String equivelentPrimaryUOMUnit;
}
