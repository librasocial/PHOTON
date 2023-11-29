package com.ssf.inward.dtos;

import com.ssf.inward.constant.InwardTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InwardPatchDto {
    private InwardTypeEnum type;
    private Map<String, Object>  properties;
}
