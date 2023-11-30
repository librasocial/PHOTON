package com.ssf.inward.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.inward.constant.InwardConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PoDetailsDto {
    private String poId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = InwardConst.ISO_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InwardConst.ISO_DATE_FORMAT)
    private LocalDate poDate;
    private Integer poAmt;
    private String remarks;
}
