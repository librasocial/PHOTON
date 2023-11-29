package com.ssf.ancregistration.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PregnantWomanDto {
    public Integer weight;
    public Integer height;
    public String bloodGroup;
}
