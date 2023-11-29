package com.ssf.immunizationrec.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssf.immunizationrec.constant.ImmunizationRecConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImmunizationRecDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String citizenId;
    private String uhid;
    private String patientId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    private LocalDate recommendedDate;
    private List<RecommendationsDto> recommendations;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
