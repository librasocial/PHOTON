package com.ssf.bssurveillance.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LabResultDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime resultDate;
    private String techicianName;
    private String result;
    private List<String> reportImages;
}
