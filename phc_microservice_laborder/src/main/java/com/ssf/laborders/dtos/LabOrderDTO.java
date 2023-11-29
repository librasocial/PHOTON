package com.ssf.laborders.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssf.laborders.constants.Constant;
import com.ssf.laborders.constants.OrderStatus;
import com.ssf.laborders.entities.Encounter;
import com.ssf.laborders.entities.MedicalTest;
import com.ssf.laborders.entities.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabOrderDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private Patient patient;
    private String careContext;
    private String assignedToLab;
    private String originatedBy;
    private Boolean isStat;
    private OrderStatus status;
    private Encounter encounter;
    private List<MedicalTest> medicalTests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime orderDate;
}
