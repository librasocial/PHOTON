package com.ssf.idspsurveillance.dtos;

import com.ssf.idspsurveillance.constant.IDSPTypeEnum;
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
    private IDSPTypeEnum type;
    private Map<String, Object>  properties;
}
