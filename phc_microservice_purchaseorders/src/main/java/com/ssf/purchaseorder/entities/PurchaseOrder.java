package com.ssf.purchaseorder.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.purchaseorder.constant.PurchaseOrderConst;
import com.ssf.purchaseorder.dtos.AuditDto;
import com.ssf.purchaseorder.dtos.IndentItemDto;
import com.ssf.purchaseorder.dtos.PoDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = PurchaseOrderConst.PHURCHASE_ORDER_COLLECTION_NAME)
public class PurchaseOrder extends  AuditDto {
    @Id
    private String id;
    private String supplierName;
    private String type;
    private String remarks;
    private PoDetails poDetails;
    private List<IndentItem> indentItems;
    private String status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PurchaseOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PurchaseOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime poDate;
}
