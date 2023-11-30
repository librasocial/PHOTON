package com.ssf.ancregistration.entities;

import com.ssf.ancregistration.constant.ANCRegistrationConst;
import com.ssf.ancregistration.dtos.AuditDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;


@Document(collection = ANCRegistrationConst.ANC_REG_COLLECTION_NAME)
@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ANCRegistration extends AuditDto {
    @Id
    public String id;
    public Couple couple;
    public MensuralPeriod mensuralPeriod;
    public PregnantWoman pregnantWoman;
    public List<String> medicalHistory;
    public Integer countPregnancies;
    public List<Pregnancy> pregnancies;
    public String expectedDeliveryFacilityType;
    public String expectedDeliveryFacility;
    public Boolean isVDRLTestCompleted;
    public LocalDate vdrlTestDate;
    public String vdrlTestResult;
    public Boolean isHIVTestCompleted;
    public LocalDate hivTestDate;
    public String hivTestResult;
    public Boolean isHBsAGTestCompleted;
    public LocalDate hbsagTestDate;
    public String hbsagTestResult;
    public String dataEntryStatus;
}
