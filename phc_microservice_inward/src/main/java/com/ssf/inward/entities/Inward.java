package com.ssf.inward.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.inward.constant.InwardConst;
import com.ssf.inward.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = InwardConst.INWARD_COLLECTION_NAME)
public class Inward extends AuditDto {
    @Id
    private String id;
    private String supplierName;
    private String inwardType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = InwardConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InwardConst.ISO_DATE_FORMAT)
    private LocalDate receivedDate;
    private PoDetails poDetails;
    private IndentDetails indentDetails;
    private List<IndentItem> indentItems;
    private String status;
}
