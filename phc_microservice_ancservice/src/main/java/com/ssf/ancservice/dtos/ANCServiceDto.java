package com.ssf.ancservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ancservice.constant.ANCServiceConst;
import com.ssf.ancservice.constant.DataEntryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ANCServiceDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @NotNull(message = "rchId should not be null")
    private String rchId;
    @NotNull(message = "ashaWorker should not be null")
    private String ashaWorker;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    private LocalDate lmpDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCServiceConst.ISO_DATE_FORMAT)
    private LocalDate eddDate;
    private String ancFacilityType;
    private String ancFacilityName;
    private DataEntryStatusEnum dataEntryStatus;
    @Valid
    private CoupleDto couple;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
