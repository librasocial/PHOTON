package com.ssf.pncservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncservice.constant.PNCServiceConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InfantDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @NotNull(message = "rchId should not be null")
    private String childId;
    private String pncServiceId;
    @NotNull(message = "ashaWorker should not be null")
    private String ashaWorker;
    private BigDecimal birthWeight;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCServiceConst.ISO_DATE_FORMAT)
    private LocalDate dateOfBirth;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
