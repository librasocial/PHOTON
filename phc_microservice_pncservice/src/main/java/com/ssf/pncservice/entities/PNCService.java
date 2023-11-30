package com.ssf.pncservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import com.ssf.pncservice.dtos.AuditDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;

@Document(collection = PNCServiceConst.PNC_SERVICE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PNCService extends AuditDto  {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String rchId;
    private String ashaWorker;
    private String deliveryType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    private LocalDate deliveryDate;
    @Valid
    private Couple couple;
}
