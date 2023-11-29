package com.ssf.bssurveillance.controller.constant;

import com.ssf.bssurveillance.dtos.BSSurveillanceDto;
import com.ssf.bssurveillance.dtos.SampleDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String BSS_ID = "123-456-789-123";
    public static final String CITIZEN_ID = "121-456-789-123";
    public static final  String SLIDE_NO ="test_s131";
    public static final  String TYPE ="MALERIA";

    public static BSSurveillanceDto buildSurveillanceDto() {
        return BSSurveillanceDto.builder()
                .id(BSS_ID)
                .citizenId(CITIZEN_ID)
                .sample(buildSampleDto())
                .type(TYPE)
                .build();
    }

    private static SampleDto buildSampleDto() {
        return SampleDto.builder()
                .slideNo(SLIDE_NO).build();
    }
}
