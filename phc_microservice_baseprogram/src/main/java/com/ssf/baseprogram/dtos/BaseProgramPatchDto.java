package com.ssf.baseprogram.dtos;

import com.ssf.baseprogram.constant.BaseProgramTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseProgramPatchDto {
    private BaseProgramTypeEnum type;
    private Map<String, Object>  properties;
}
