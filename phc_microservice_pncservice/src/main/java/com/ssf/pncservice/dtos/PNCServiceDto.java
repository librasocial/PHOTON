package com.ssf.pncservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
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
public class PNCServiceDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @NotNull(message = "rchId should not be null")
    private String rchId;
    @NotNull(message = "ashaWorker should not be null")
    private String ashaWorker;
    private String deliveryType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    private LocalDate deliveryDate;
    @Valid
    private CoupleDto couple;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
