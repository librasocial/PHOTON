package com.ssf.purchaseorder.entities;

import com.ssf.purchaseorder.constant.PurchaseOrderConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = PurchaseOrderConst.PHURCHASE_ORDER_COLLECTION_NAME)
public class PoItem {
    private String productId;
    private String productName;
    private String uOM;
    private Integer quantity;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal rate;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cgstPercentage;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal sgstPercentage;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cgstAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal sgstAmount;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalAmount;
}
