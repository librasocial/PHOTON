package com.ssf.immunizationrec.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.immunizationrec.constant.ImmunizationRecConst;
import com.ssf.immunizationrec.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(collection = ImmunizationRecConst.IMMUN_REC_COLLECTION_NAME)
public class ImmunizationRec extends  AuditDto {
    private String id;
    private String citizenId;
    private String uhid;
    private String patientId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ImmunizationRecConst.ISO_DATE_FORMAT)
    private LocalDate recommendedDate;
    private List<Recommendations> recommendations;

}
