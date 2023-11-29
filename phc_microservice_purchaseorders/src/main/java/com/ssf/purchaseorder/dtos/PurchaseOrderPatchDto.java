package com.ssf.purchaseorder.dtos;

import com.ssf.purchaseorder.constant.PurchaseOrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderPatchDto {
    private PurchaseOrderTypeEnum type;
    private Map<String, Object>  properties;
}
