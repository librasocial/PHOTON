package com.ssf.ancservice.dtos;

import com.ssf.ancservice.constant.ANCServicePatchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ANCServicePatchDto {
    private ANCServicePatchType type;
    private Map<String, Object> properties;
}