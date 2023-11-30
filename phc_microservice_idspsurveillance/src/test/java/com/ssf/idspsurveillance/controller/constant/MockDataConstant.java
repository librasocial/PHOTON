package com.ssf.idspsurveillance.controller.constant;


import com.ssf.idspsurveillance.dtos.IDSPSurveillanceDto;
import com.ssf.idspsurveillance.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String IDSP_ID = "123-456-789-123";
    public static final String CITIZEN_ID = "121-456-789-123";
    public static IDSPSurveillanceDto builIDSPdto() {
        return IDSPSurveillanceDto.builder()
                .id(IDSP_ID)
                .citizenId(CITIZEN_ID)
                .dateOfSurveillance(LocalDateTime.of(2022,9,8,23,44,10))
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
