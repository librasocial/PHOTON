package com.ssf.pncregistration.constant;

import com.ssf.pncregistration.dtos.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String PNCID = "123-456-789-123";
    public static final String INVALID_PNCID = "123-789-123";
    public static final String RCHID = "423-456-789-121";
    public static final  String REG_ID ="76223232";
    public static final  String INFANT_ID ="1217-8128-2121";
    public static final String HUSBANDID = "test_hus1";
    public static final String WIFEID = "test_wif1";


    public static CoupleDto buildCoupleDto() {
        return CoupleDto.builder()
                .husbandId(HUSBANDID)
                .rchId(RCHID)
                .wifeId(WIFEID).build();
    }
    public static MensuralPeriodDto buildMensuralPeriodDto() {
        return MensuralPeriodDto.builder()
                .lmpDate(LocalDate.now())
                .eddDate(LocalDate.now())
                .build();
    }

    public static DeliveryDetailsDto buildDeliveryDto() {
        return DeliveryDetailsDto.builder()
                .deliveryDate(LocalDateTime.now())
                .financialYear("1988-89")
                .place("Banaglore")
                .build();
    }

    public static PNCRegistrationDto buildPNCRegDto() {
        return PNCRegistrationDto.builder()
                .id(PNCID)
                .couple(buildCoupleDto())
                .mensuralPeriod(buildMensuralPeriodDto())
                .dataEntryStatus(DataEntryStatusEnum.DRAFT)
                .deliveryDetails(buildDeliveryDto())
                .build();
    }

    public static ImmunizationDto buildImmunizationo() {
        return ImmunizationDto.builder()
                .bcgDoseDate(LocalDate.now())
                .hepB0DoseDate(LocalDate.now())
                .build();
    }
    public static InfantDto buildInfantDto() {
        return InfantDto.builder()
                .id(INFANT_ID)
                .pncRegistrationId(PNCID)
                .immunization(buildImmunizationo())
                .build();
    }

}
