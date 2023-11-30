package com.ssf.childcareservice.config.constant;

import com.ssf.childcareservice.dtos.ChildCareDto;
import com.ssf.childcareservice.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String CHILD_CARE_ID = "4231-456-789-1211";
    public static final  String IMMUN_REC_ID ="123-456-789-123";
    public static final  String CITIZEN_ID ="124-456-789-124";
    public static final  String CHILD_ID ="123-456-789-124";



    public static ChildCareDto buildSampleChildCareDto() {
        return ChildCareDto.builder()
                .id(CHILD_CARE_ID)
                .citizenId(CITIZEN_ID)
                .childId(CHILD_ID)
                .immunizationRecommendationId(IMMUN_REC_ID)
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
