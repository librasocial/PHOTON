package com.ssf.pncservice.dtos;

import com.ssf.pncservice.constant.PNCServicePatchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PNCServicePatchDto {
    private PNCServicePatchType type;
    private Map<String, Object> properties;
}