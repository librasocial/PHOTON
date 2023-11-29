package com.ssf.eligiblecouple.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
import com.ssf.eligiblecouple.dtos.AuditDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = EligibleCoupleConst.VISIT_LOG_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class VisitLog extends AuditDto {

    @Id
    private String id;
    private String rchId;
    private String serviceId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime visitDate;
    private Boolean isPregnancyTestTaken;
    private String pregnancyTestResult;
    private Boolean isReferredToPHC;
    private String bcOcpType;
    private String bcQuantity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)

    private LocalDateTime pcContraceptiveStopDate;
    private String serviceType;
}
