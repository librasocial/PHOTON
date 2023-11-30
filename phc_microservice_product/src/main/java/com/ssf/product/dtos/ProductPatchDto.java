package com.ssf.product.dtos;

import com.ssf.product.constant.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPatchDto {
    private ProductTypeEnum type;
    private Map<String, Object>  properties;
}
