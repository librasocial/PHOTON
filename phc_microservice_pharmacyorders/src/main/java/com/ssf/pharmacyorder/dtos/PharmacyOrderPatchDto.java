package com.ssf.pharmacyorder.dtos;

import com.ssf.pharmacyorder.constant.PharmacyOrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PharmacyOrderPatchDto {
    private PharmacyOrderTypeEnum type;
    private Map<String, Object>  properties;
}
