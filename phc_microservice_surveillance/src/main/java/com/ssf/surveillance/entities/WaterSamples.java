package com.ssf.surveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.surveillance.constants.Constant;
import com.ssf.surveillance.dtos.AuditDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = Constant.WATER_SAMPLES_COLLECTION)
public class WaterSamples {


    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate dateOfSurveillance;

    private String villageId;

    private String placeType;

    private String placeOrgId;

    private String placeName;

    private String householdId;

    private TestResult testResult;

    private Sample sample;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDTO audit;
}
