package com.ssf.eligiblecouple.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
import com.ssf.eligiblecouple.constant.PregnancyTestEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class VisitLogDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String rchId;
    private String serviceId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime visitDate;
    private Boolean isPregnancyTestTaken;
    private PregnancyTestEnum pregnancyTestResult;
    private Boolean isReferredToPHC;
    private String bcOcpType;
    private String bcQuantity;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = EligibleCoupleConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime pcContraceptiveStopDate;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
    private String serviceType;
}
