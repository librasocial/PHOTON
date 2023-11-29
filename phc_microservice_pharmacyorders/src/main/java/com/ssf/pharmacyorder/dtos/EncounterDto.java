package com.ssf.pharmacyorder.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pharmacyorder.constant.PharmacyOrderConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EncounterDto {
    private String encounterId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PharmacyOrderConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime encounterDateTime;
    private String staffId;
    private String staffName;
    private String prescriptionId;
}
