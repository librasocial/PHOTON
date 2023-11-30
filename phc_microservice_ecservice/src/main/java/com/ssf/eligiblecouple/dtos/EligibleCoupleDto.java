package com.ssf.eligiblecouple.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.eligiblecouple.constant.DataEntryStatusEnum;
import com.ssf.eligiblecouple.constant.EligibleCoupleConst;
import com.ssf.eligiblecouple.constant.PregnancyTestEnum;
import com.ssf.eligiblecouple.constant.ServiceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class EligibleCoupleDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @NotNull(message = "rchId should not be null")
    private String rchId;
    @NotNull(message = "ashaWorker should not be null")
    private String ashaWorker;
    private ServiceTypeEnum serviceType;
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
    private PregnancyTestEnum pregnancyTestResult;
    private DataEntryStatusEnum dataEntryStatus;
    @Valid
    private CoupleDto couple;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;

}
