package com.ssf.surveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.surveillance.constants.Constant;
import com.ssf.surveillance.dtos.AuditDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = Constant.IODINE_SAMPLES_COLLECTION)
public class IodineSamples {

    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    private String iodineSurveillanceId;

    private String shopOwnerName;

    private String saltBrandName;

    private Integer noOfSamplesCollected;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate dateCollected;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate labSubmittedDate;

    private String labTestResultStatus;

    private List<String> reportImageUrl;

    private Integer iodineResultQty;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.ISO_DATE_FORMAT)
    private LocalDate resultDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDTO audit;

}


