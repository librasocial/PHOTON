package com.ssf.idspsurveillance.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.idspsurveillance.constant.IDSPConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class IDSPSurveillanceDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = IDSPConstant.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IDSPConstant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateOfSurveillance;
    private String citizenId;
    private String symptomType;
    private FeverDto fever;
    private CoughDto cough;
    private LooseStoolsDto looseStools;
    private JaundiceDto jaundice;
    private AnimalBiteDto animalBite;
    private OthersDto others;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = IDSPConstant.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IDSPConstant.ISO_DATE_FORMAT)
    private LocalDate dateOfDeath;
    private Boolean isReferredToPhc;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
