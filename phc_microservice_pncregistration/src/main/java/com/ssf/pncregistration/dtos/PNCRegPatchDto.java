package com.ssf.pncregistration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PNCRegPatchDto {
    private PNCCRegTypeEnum type;
    private Map<String, Object>  properties;
}
