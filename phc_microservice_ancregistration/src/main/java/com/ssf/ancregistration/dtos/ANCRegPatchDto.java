package com.ssf.ancregistration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ANCRegPatchDto {
    private ANCCRegTypeEnum type;
    private Map<String, Object>  properties;
}
