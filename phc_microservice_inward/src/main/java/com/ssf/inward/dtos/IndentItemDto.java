package com.ssf.inward.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.inward.constant.InwardConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class IndentItemDto {
    private String productId;
    private String productName;
    private String barCode;
    private String uom;
    private String batchNumber;
    private Integer quantity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = InwardConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InwardConst.ISO_DATE_FORMAT)
    private LocalDate mfgDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = InwardConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InwardConst.ISO_DATE_FORMAT)
    private LocalDate expiryDate;
    private BigDecimal rate;
    private BigDecimal mrpRate;
    private BigDecimal cgstPercentage;
    private BigDecimal sgstPercentage;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
}
