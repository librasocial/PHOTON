package com.ssf.idspsurveillance.entites;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.idspsurveillance.constant.IDSPConstant;
import com.ssf.idspsurveillance.dtos.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class IDSPSurveillance extends  AuditDto {

    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = IDSPConstant.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IDSPConstant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dateOfSurveillance;
    private String citizenId;
    private String symptomType;
    private Fever fever;
    private Cough cough;
    private LooseStools looseStools;
    private Jaundice jaundice;
    private AnimalBite animalBite;
    private Others others;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = IDSPConstant.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = IDSPConstant.ISO_DATE_FORMAT)
    private LocalDate dateOfDeath;
    private Boolean isReferredToPhc;
}
