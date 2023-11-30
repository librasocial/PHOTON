package com.ssf.inward.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.inward.constant.InwardConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = InwardConst.INWARD_COLLECTION_NAME)
public class IndentDetails {
    private String deliveryChallanNo;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = InwardConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InwardConst.ISO_DATE_FORMAT)
    private LocalDate challanDate;
    private String remarks;
}
