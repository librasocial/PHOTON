package com.ssf.pncregistration.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssf.pncregistration.constant.PNCRegistrationConst;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Document(collection = PNCRegistrationConst.PNC_REG_COLLECTION_NAME)
public class Couple {
    private String husbandId;
    private String husbandName;
    private String wifeId;
    private String wifeName;
    private Integer ecSerialNumber;
    private String registeredBy;
    private String registeredByName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime registeredOn;
    private String rchId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PNCRegistrationConst.ISO_DATE_TIME_FORMAT)
    private LocalDateTime lastANCVisitDate;
}
