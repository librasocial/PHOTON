package com.ssf.pncservice.constant;

import com.ssf.pncservice.dtos.CoupleDto;
import com.ssf.pncservice.dtos.InfantVisitLogDto;
import com.ssf.pncservice.dtos.PNCServiceDto;
import com.ssf.pncservice.dtos.VisitLogDto;
import com.ssf.pncservice.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String PNCID = "123-456-789-123";
    public static final String INVALID_PNCID = "123-789-123";
    public static final String RCHID = "423-456-789-121";
    public static final String HUSBANDID = "test_hus1";
    public static final String WIFEID = "test_wif1";
    public static final String ASHA_WORKER = "test_asha_name";
    public static final String LOG_ID = "4231-456-789-1211";
    public static final String SERVICE_ID = "123-456-789-123";
    public static final String CHILD_ID = "124-456-789-124";


    public static CoupleDto buildCoupleDto() {
        return CoupleDto.builder()
                .husbandId(HUSBANDID)
                .wifeId(WIFEID).build();

    }

    public static PNCServiceDto buildPNCServiceDto() {
        return PNCServiceDto.builder()
                .id(PNCID)
                .rchId(RCHID)
                .ashaWorker(ASHA_WORKER)
                .couple(buildCoupleDto()).build();
    }

    public static VisitLogDto buildSampleVislitLogDto() {
        return VisitLogDto.builder()
                .id(LOG_ID)
                .serviceId(SERVICE_ID)
                .rchId(RCHID)
                .pncDate(LocalDateTime.now())
                .build();
    }

    public static InfantVisitLogDto buildSampleInfantVislitLogDto() {
        return InfantVisitLogDto.builder()
                .id(LOG_ID)
                .serviceId(SERVICE_ID)
                .childId(RCHID)
                .pncDate(LocalDate.now())
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
