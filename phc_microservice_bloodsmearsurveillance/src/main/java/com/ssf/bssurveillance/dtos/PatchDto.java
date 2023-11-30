package com.ssf.bssurveillance.dtos;

import com.ssf.bssurveillance.constant.BSSurveillanceConstTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchDto {
    private BSSurveillanceConstTypeEnum type;
    private Map<String, Object>  properties;
}
