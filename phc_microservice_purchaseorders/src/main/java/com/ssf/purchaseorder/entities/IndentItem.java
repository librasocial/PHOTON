package com.ssf.purchaseorder.entities;

import com.ssf.purchaseorder.constant.PurchaseOrderConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = PurchaseOrderConst.PHURCHASE_ORDER_COLLECTION_NAME)
public class IndentItem {
    private String productId;
    private String productName;
    private String uom;
    private Integer quantity;
}
