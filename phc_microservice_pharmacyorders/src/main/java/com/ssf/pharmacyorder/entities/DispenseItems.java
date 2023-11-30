package com.ssf.pharmacyorder.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pharmacyorder.constant.PharmacyOrderConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = PharmacyOrderConst.PHARMACY_DISPENSE_COLLECTION_NAME)
public class DispenseItems {
    private String productId;
    private String productName;
    private String batchNo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PharmacyOrderConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PharmacyOrderConst.ISO_DATE_FORMAT)
    private LocalDate expiryDate;
    private Integer orderQty;
    private Integer qtyIssued;
}
