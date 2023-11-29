package com.ssf.pncservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import com.ssf.pncservice.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = PNCServiceConst.PNC_INFANT_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Infant extends AuditDto {
    private String id;
    private String childId;
    private String pncServiceId;
    private String ashaWorker;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal birthWeight;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    private LocalDate dateOfBirth;
}
