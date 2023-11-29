package com.ssf.purchaseorder.entities;

import com.ssf.purchaseorder.constant.PurchaseOrderConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor@Document(value = PurchaseOrderConst.PHURCHASE_ORDER_COLLECTION_NAME)

public class PoDetails {
    private List<PoItem> poItems;
    private Integer netAmount;
    private Integer gst;
    private Integer grandTotal;
}
