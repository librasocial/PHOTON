package com.ssf.laborders.dtos;

import com.ssf.laborders.constants.OrderSampleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSamplesPatchDTO {
    OrderSampleTypeEnum type;
    Map<String, Object> properties;
}
