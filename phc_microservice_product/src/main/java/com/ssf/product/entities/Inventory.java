package com.ssf.product.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.product.constant.ProductConst;
import com.ssf.product.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = ProductConst.INVENTORY_COLLECTION_NAME)
public class Inventory  extends AuditDto  {
    @Id
    private String id;
    private  String productId;
    private String name;
    private String batchNumber;
    private String status;
    private BigDecimal level;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ProductConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ProductConst.ISO_DATE_FORMAT)
    private LocalDate expiryDate;
}
