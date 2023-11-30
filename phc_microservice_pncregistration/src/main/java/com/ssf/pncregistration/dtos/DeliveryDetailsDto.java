package com.ssf.pncregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class DeliveryDetailsDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime deliveryDate;
    private String  financialYear;
    private String place;
    private String location;
    private String conductedBy;
    private String deliveryType;
    private Integer deliveryOutcome;
    private Integer liveBirthCount;
    private Integer stillBirthCount;
    private String complications;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime dischargeDateTime;
}
