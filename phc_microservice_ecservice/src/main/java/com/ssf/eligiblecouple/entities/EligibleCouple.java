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
import java.util.List;

@Document(collection = EligibleCoupleConst.ELIGIBLE_COUPLE_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EligibleCouple extends AuditDto {

    @Id
    private String id;
    private String rchId;
    private String ashaWorker;
    private String serviceType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime visitDate;
    private String financialYear;
    private Boolean isReferredToPHC;
    private String bcServiceOfferedTo;
    private String bcTypeOfContraceptive;
    private String bcContraceptiveQuantity;
    private List<String> pcServiceList;
    private Boolean pcHasStoppedContraceptive;
    private Boolean isPregnancyTestTaken;
    private String pregnancyTestResult;
    private Couple couple;
    private String dataEntryStatus;
}
