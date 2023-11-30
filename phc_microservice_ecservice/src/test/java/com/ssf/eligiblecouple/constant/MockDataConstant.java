package com.ssf.eligiblecouple.constant;

import com.ssf.eligiblecouple.dtos.CoupleDto;
import com.ssf.eligiblecouple.dtos.EligibleCoupleDto;
import com.ssf.eligiblecouple.dtos.VisitLogDto;
import com.ssf.eligiblecouple.entities.Couple;
import com.ssf.eligiblecouple.entities.EligibleCouple;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String SERVICE_ID = "76223232";
    public static final String LOG_ID = "1231312";
    public static final String RCHID = "rchd1_test";
    public static final String ASHA_WORKER = "asha_test";
    public static final String BC_SERVICE_OFFER_TO = "offer_test";
    public static final List<String> PC_SERVICE_LIST = Arrays.asList("pc1_test", "pc2_test");
    public static final String WIFE_ID = "wife_test";
    public static final String HUSBAND_ID = "hus_test";
    public static final String INVALID_SERVICE_ID = "762232321";
    public static final String ID_TOKEN = "eyJraWQiOiIyRHBKUjVDQ05BNllRXC9VU0oyWE9qV3RudXcxR1VsZnlsdUFxaTZrcm43ST0iLCJhbGciOiJSUzI1NiJ9.eyJvcmlnaW5fanRpIjoiYzJiOGI1YjQtOGI1Ny00OTBkLTlkMWQtZTQ3ZDU4ZGNjOGI4Iiwic3ViIjoiODFiN2VhYmMtYWM4Ny00MWZlLWI2NzUtM2I0OWIzNDYzNmJjIiwiYXVkIjoiM3Y5c21iajF0ZHNnb3VrZmpmNGx0bmowN2MiLCJldmVudF9pZCI6ImFkYjM5MGRhLTI0NjYtNDlkYi1hNmI4LTExY2NhZDQyY2ZmNCIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjU1MjE4OTc1LCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuYXAtc291dGgtMS5hbWF6b25hd3MuY29tXC9hcC1zb3V0aC0xX2lpY1pGWHA5TyIsImNvZ25pdG86dXNlcm5hbWUiOiJyYWtlc2gtZGV2IiwiZXhwIjoxNjU1MzA1Mzc0LCJpYXQiOjE2NTUyMTg5NzUsImp0aSI6IjNjMTQ2NDNmLWY3NmItNDcxMC04MWY1LWI0ZTMyMWU5M2U2YSJ9.SS3rxIeAvTlzM2JAqNiwHzq3QA659WbDhSIZNpxnsEH9UW4kqn1a2rbDVCQsexhYkNVog6Ge_lKCHXxsdy7oGQzqdOjN6wbXjteg_AlcdcR5tOnFls5cvzq44OrkQ4uwzpeoYBy2_8a9hniWVhrSmE5tD253n8E2m-dszaWdmWZeJ9jZmjd_V6THfjfKatNN7Hs2TzLGq6IodXQEj-05uqPcGDBRWHN79WcwHnUn3g8FXZoNkcQ2ih_ypiKE1yeZoFSDMTlwGdZAwCHaAmHdw-vR19GQpyEAmIrqLSwkeZ3awVu6vrgMefwbN3nhJeWIseMTmvhfoQdVdFi5gik6jg";

    public static EligibleCouple buildSampleEligibleCouplEntity() {
        EligibleCouple expectedResponse = EligibleCouple.builder()
                .id(SERVICE_ID)
                .rchId(RCHID)
                .bcServiceOfferedTo(BC_SERVICE_OFFER_TO)
                .ashaWorker(ASHA_WORKER)
                .pcServiceList(PC_SERVICE_LIST)
                .couple(Couple.builder().wifeId(WIFE_ID).husbandId(HUSBAND_ID).build())
                .build();
        return expectedResponse;
    }

    public static EligibleCoupleDto buildSampleEligibleCoupleDto() {
        EligibleCoupleDto expectedResponse = EligibleCoupleDto.builder()
                .id(SERVICE_ID)
                .rchId(RCHID)
                .bcServiceOfferedTo(BC_SERVICE_OFFER_TO)
                .ashaWorker(ASHA_WORKER)
                .serviceType(ServiceTypeEnum.BC)
                .bcTypeOfContraceptive("test_bctype")
                .bcContraceptiveQuantity("test_bctype_quantity")
                .pcHasStoppedContraceptive(true)
                .isPregnancyTestTaken(true)
                .isReferredToPHC(true)
                .financialYear("2022-23")
                .pcServiceList(PC_SERVICE_LIST)
                .couple(CoupleDto.builder().wifeId(WIFE_ID).husbandId(HUSBAND_ID).build())
                .dataEntryStatus(DataEntryStatusEnum.DRAFT)
                .build();
        return expectedResponse;
    }

    public static VisitLogDto buildSampleVislitLogDto() {
        return VisitLogDto.builder()
                .id(LOG_ID)
                .bcOcpType("test_oc")
                .bcQuantity("test_bc_qu")
                .pcContraceptiveStopDate(LocalDateTime.now())
                .pregnancyTestResult(PregnancyTestEnum.POSITIVE)
                .isPregnancyTestTaken(true)
                .isReferredToPHC(true)
                .serviceId(SERVICE_ID)
                .rchId(RCHID)
                .visitDate(LocalDateTime.now())
                .build();
    }
}
