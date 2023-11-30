package com.ssf.pharmacyorder.entities;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Items {
    private String productId;
    private String productName;
    private Integer orderQty;
}
