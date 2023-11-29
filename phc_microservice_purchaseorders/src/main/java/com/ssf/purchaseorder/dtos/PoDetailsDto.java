package com.ssf.purchaseorder.dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PoDetailsDto {
    private List<PoItemDto> poItems;
    private Integer netAmount;
    private Integer gst;
    private Integer grandTotal;
}
