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
@Document(collection = Constant.LARVA_VISITS_COLLECTION)
public class LarvaVisits {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String larvaSurveillanceId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate visitDate;

    private String isLarvaFound;

    private String breedingSpotName;
    
    private String breedingSite;

    private String solutionProvided;

    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate nextInspectionDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDTO audit;

}
