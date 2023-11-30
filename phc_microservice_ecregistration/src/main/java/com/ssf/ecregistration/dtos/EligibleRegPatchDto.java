package com.ssf.ecregistration.dtos;

import com.ssf.ecregistration.constant.ECRegTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EligibleRegPatchDto {
    private ECRegTypeEnum type;
    private Map<String, Object>  properties;
}
