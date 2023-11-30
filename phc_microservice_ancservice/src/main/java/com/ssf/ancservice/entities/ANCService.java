package com.ssf.ancservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ancservice.constant.ANCServiceConst;
import com.ssf.ancservice.constant.DataEntryStatusEnum;
import com.ssf.ancservice.dtos.AuditDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Document(collection = ANCServiceConst.ANC_SERVICE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ANCService {
    @Id
    private String id;
    private String rchId;
    private String ashaWorker;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    private LocalDate lmpDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    private LocalDate eddDate;
    private String ancFacilityType;
    private String ancFacilityName;
    private Couple couple;
    private DataEntryStatusEnum dataEntryStatus;
    private AuditDto audit;
}
