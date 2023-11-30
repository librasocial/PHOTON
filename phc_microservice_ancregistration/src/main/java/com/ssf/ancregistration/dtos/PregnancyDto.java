package com.ssf.ancregistration.dtos;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PregnancyDto {
    public Integer pregnancyNo;
    public List<String> complication;
    public String outcome;
}
