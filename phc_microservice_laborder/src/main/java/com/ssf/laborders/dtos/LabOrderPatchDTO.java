package com.ssf.laborders.dtos;

import com.ssf.laborders.constants.LabOrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabOrderPatchDTO {
    LabOrderTypeEnum type;
    Map<String, Object> properties;
}
