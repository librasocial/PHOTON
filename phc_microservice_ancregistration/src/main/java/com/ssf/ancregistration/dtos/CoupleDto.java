package com.ssf.ancregistration.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.ancregistration.constant.ANCRegistrationConst;
import com.ssf.ancregistration.validator.UniqueHusband;
import com.ssf.ancregistration.validator.UniqueWife;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CoupleDto {
    @UniqueHusband
    public String husbandId;
    public String husbandName;
    public String husbandPhone;
    @UniqueWife
    public String wifeId;
    public String wifeName;
    public String wifePhone;
    public String address;
    public Integer ecSerialNumber;
    public String registeredBy;
    public String registeredByName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = ANCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ANCRegistrationConst.ISO_DATE_TIME_FORMAT)
    public LocalDateTime registeredOn;
    public String rchId;
}
