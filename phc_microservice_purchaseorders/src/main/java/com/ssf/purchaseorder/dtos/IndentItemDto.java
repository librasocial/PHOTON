package com.ssf.purchaseorder.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class IndentItemDto {
    private String productId;
    private String productName;
    private String uom;
    private Integer quantity;
}
