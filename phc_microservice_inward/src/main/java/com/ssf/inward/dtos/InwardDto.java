package com.ssf.inward.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.inward.constant.InwardConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class InwardDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    private String supplierName;
    private String inwardType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = InwardConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InwardConst.ISO_DATE_FORMAT)
    private LocalDate receivedDate;
    private PoDetailsDto poDetails;
    private IndentDetailsDto indentDetails;
    private List<IndentItemDto> indentItems;
    private String status;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuditDto audit;
}
