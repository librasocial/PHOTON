package com.ssf.product.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.product.constant.InventoryStatusEnum;
import com.ssf.product.constant.ProductConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InventoryDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private  String productId;
    private String name;
    private String batchNumber;
    private InventoryStatusEnum status;
    private BigDecimal level;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ProductConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ProductConst.ISO_DATE_FORMAT)
    private LocalDate expiryDate;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
