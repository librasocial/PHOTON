package com.ssf.baseprogram.constant;

import com.ssf.baseprogram.dtos.BaseProgramDto;
import com.ssf.baseprogram.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String BASE_PROGRAM_ID = "123-456-789-123";
    public static final String ACTIVITY_NAME = "TEST_ACTIVITY";
    public static final String PROGRAM_TYPE = "VHND";
    public static final LocalDateTime MEETING_DATE = LocalDateTime.of(2020, 9, 12, 10, 12,30);

    public static BaseProgramDto buildBaseProgramDto() {
        return BaseProgramDto.builder()
                .id(BASE_PROGRAM_ID)
                .activityName(ACTIVITY_NAME)
                .meetingDate(MEETING_DATE)
                .programType(PROGRAM_TYPE)
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
