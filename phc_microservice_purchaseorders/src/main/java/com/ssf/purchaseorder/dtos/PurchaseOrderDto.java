package com.ssf.purchaseorder.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.purchaseorder.constant.PurchaseOrderConst;
import com.ssf.purchaseorder.constant.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PurchaseOrderDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String supplierName;
    private String type;
    private String remarks;
    private PoDetailsDto poDetails;
    private List<IndentItemDto> indentItems;
    private StatusEnum status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PurchaseOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PurchaseOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime poDate;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
