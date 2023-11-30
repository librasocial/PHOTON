package com.ssf.bssurveillance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.bssurveillance.constant.BSSurveillanceConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Document(value = BSSurveillanceConst.BSS_COLLECTION_NAME)
public class LabResult {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BSSurveillanceConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime resultDate;
    private String techicianName;
    private String result;
    private List<String> reportImages;
}
