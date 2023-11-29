package com.ssf.pharmacyorder.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pharmacyorder.constant.PharmacyOrderConst;
import com.ssf.pharmacyorder.constant.StatusEnum;
import com.ssf.pharmacyorder.dtos.AuditDto;
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
@Document(collection = PharmacyOrderConst.PHARMACY_ORDER_COLLECTION_NAME)
public class PharmacyOrder extends AuditDto {
    @Id
    private String id;
    private Patient patient;
    private StatusEnum status;
    private Encounter encounter;
    private List<Items> items;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime orderDate;
    private String originatedBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime registeredDate;
}
