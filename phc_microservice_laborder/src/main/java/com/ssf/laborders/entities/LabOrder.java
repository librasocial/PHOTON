package com.ssf.laborders.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.laborders.constants.Constant;
import com.ssf.laborders.constants.OrderStatus;
import com.ssf.laborders.dtos.AuditDTO;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = Constant.LAB_ORDERS_COLLECTION)
public class LabOrder {
    @Id
    private String id;
    private String careContext;

    private Patient patient;

    private String assignedToLab;

    private Boolean isStat;

    private OrderStatus status;

    private Encounter encounter;

    private String originatedBy;

    private List<MedicalTest> medicalTests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_TIME_FORMAT)
    private LocalDateTime orderDate;

    private AuditDTO audit;

}
