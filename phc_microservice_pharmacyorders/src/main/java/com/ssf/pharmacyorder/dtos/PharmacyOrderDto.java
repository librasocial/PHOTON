package com.ssf.pharmacyorder.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pharmacyorder.constant.PharmacyOrderConst;
import com.ssf.pharmacyorder.constant.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PharmacyOrderDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private PatientDto patient;
    private StatusEnum status;
    private EncounterDto encounter;
    private List<ItemsDto> items;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime orderDate;
    private String originatedBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime registeredDate;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
