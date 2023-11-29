package com.ssf.laborders.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.laborders.constants.Constant;
import com.ssf.laborders.dtos.AuditDTO;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = Constant.ORDERS_SUMMARY_COLLECTION)
public class OrdersSummary {
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate workingDay;

    private Long ordersCount = 0L;

    private Long malePatientCount = 0L;

    private Long femalePatientCount = 0L;

    private Long transgenderPatientCount = 0L;

    private AuditDTO audit;
}
