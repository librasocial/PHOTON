package com.ssf.ancregistration.constant;

import com.ssf.ancregistration.dtos.*;
import com.ssf.ancregistration.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String ID_TOKEN = "eyJraWQiOiIyRHBKUjVDQ05BNllRXC9VU0oyWE9qV3RudXcxR1VsZnlsdUFxaTZrcm43ST0iLCJhbGciOiJSUzI1NiJ9.eyJvcmlnaW5fanRpIjoiYzJiOGI1YjQtOGI1Ny00OTBkLTlkMWQtZTQ3ZDU4ZGNjOGI4Iiwic3ViIjoiODFiN2VhYmMtYWM4Ny00MWZlLWI2NzUtM2I0OWIzNDYzNmJjIiwiYXVkIjoiM3Y5c21iajF0ZHNnb3VrZmpmNGx0bmowN2MiLCJldmVudF9pZCI6ImFkYjM5MGRhLTI0NjYtNDlkYi1hNmI4LTExY2NhZDQyY2ZmNCIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjU1MjE4OTc1LCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuYXAtc291dGgtMS5hbWF6b25hd3MuY29tXC9hcC1zb3V0aC0xX2lpY1pGWHA5TyIsImNvZ25pdG86dXNlcm5hbWUiOiJyYWtlc2gtZGV2IiwiZXhwIjoxNjU1MzA1Mzc0LCJpYXQiOjE2NTUyMTg5NzUsImp0aSI6IjNjMTQ2NDNmLWY3NmItNDcxMC04MWY1LWI0ZTMyMWU5M2U2YSJ9.SS3rxIeAvTlzM2JAqNiwHzq3QA659WbDhSIZNpxnsEH9UW4kqn1a2rbDVCQsexhYkNVog6Ge_lKCHXxsdy7oGQzqdOjN6wbXjteg_AlcdcR5tOnFls5cvzq44OrkQ4uwzpeoYBy2_8a9hniWVhrSmE5tD253n8E2m-dszaWdmWZeJ9jZmjd_V6THfjfKatNN7Hs2TzLGq6IodXQEj-05uqPcGDBRWHN79WcwHnUn3g8FXZoNkcQ2ih_ypiKE1yeZoFSDMTlwGdZAwCHaAmHdw-vR19GQpyEAmIrqLSwkeZ3awVu6vrgMefwbN3nhJeWIseMTmvhfoQdVdFi5gik6jg";
    public static final String ANCID = "123-456-789-123";
    public static final String INVALID_ANCID = "123-789-123";
    public static final String RCHID = "423-456-789-121";
    public static final  String REG_ID ="76223232";
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
                .isReferredToPHC(false)
                .isReferredToPHC(true)
                .build();
    }

    public static PregnancyDto buildPregnancyDto() {
        return PregnancyDto.builder()
                .pregnancyNo(2)
                .complication(Arrays.asList("test"))
                .outcome("passed")
                .build();
    }

    public static PregnantWomanDto buildPregnantWomanDto() {
        return PregnantWomanDto.builder()
                .height(5)
                .weight(35)
                .bloodGroup("O+ve")
                .build();
    }

    public static ANCRegistrationDto buildANCRegDto() {
        return ANCRegistrationDto.builder()
                .id(ANCID)
                .couple(buildCoupleDto())
                .countPregnancies(10)
                .pregnantWoman(buildPregnantWomanDto())
                .pregnancies(List.of(buildPregnancyDto()))
                .mensuralPeriod(buildMensuralPeriodDto())
                .dataEntryStatus(DataEntryStatusEnum.DRAFT)
                .build();
    }

    public static ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }

}
