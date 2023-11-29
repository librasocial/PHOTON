package com.ssf.tbsurveillance.mockdataconstant;

import java.time.LocalDateTime;

import com.ssf.tbsurveillance.dtos.TbSurveillanceDto;
import com.ssf.tbsurveillance.exception.ApiSubError;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String TB_SURVEILLANCE_ID = "123-456-789-123";
    public static final String CITIZEN_ID = "123-456-789-123";
    public static final LocalDateTime MEETING_DATE = LocalDateTime.of(2020, 9, 12, 10, 12,30);

    public static TbSurveillanceDto buildTBSurveillanceDto() {
        return TbSurveillanceDto.builder()
                .id(TB_SURVEILLANCE_ID)
                .citizenId(CITIZEN_ID)
                .wasTreatedForTBInPast(false)
                .isReferredToPhc(true)
                .hasDiabetes(true)
                .hasTBSymptoms(true)
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
