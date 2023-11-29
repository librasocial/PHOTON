package com.ssf.purchaseorder.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PoItemDto {
    private String productId;
    private String productName;
    private String uOM;
    private Integer quantity;
    private BigDecimal rate;
    private BigDecimal cgstPercentage;
    private BigDecimal sgstPercentage;
    private BigDecimal cgstAmount;
    private BigDecimal sgstAmount;
    private BigDecimal totalAmount;
}
