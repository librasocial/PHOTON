package com.ssf.immunization.constant;

import com.ssf.immunization.dtos.DeathDto;
import com.ssf.immunization.dtos.FilterDto;
import com.ssf.immunization.dtos.ImmunizationDto;
import com.ssf.immunization.dtos.ReactionDto;

import com.ssf.immunization.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String IMMUN_ID = "123-456-789-123";
    public static final String INVALID_IMMUN_ID = "123-789-123";
    public static final  String CITIZEN_ID ="123-456-789-123";
    public static final  String PATIENT_ID ="124-456-789-124";
    public static final  String UHID ="124-421-789-124";
    public static final String REC_ID = "123-459-930";


    public static DeathDto buildDeathDto() {
        return DeathDto.builder()
                .cause("test")
                .place("blore")
                .build();
    }
    public static ReactionDto buildReactionDto() {
        return ReactionDto.builder()
                .caseClosureReason("test")
                .detail("test+details")
                .death(buildDeathDto())
                .build();
    }

    public static ImmunizationDto buildImmunizationDto() {
        return ImmunizationDto.builder()
                .id(IMMUN_ID)
                .citizenId(CITIZEN_ID)
                .patientId(PATIENT_ID)
                .recommendationId(REC_ID)
                .reaction(buildReactionDto())
                .build();
    }

    public  static FilterDto buildFullFilterDto() {
        return  FilterDto.builder()
                .citizenId(CITIZEN_ID)
                .patientId(PATIENT_ID)
                .uhid(UHID)
                .recommendationId(REC_ID)
                .pageNumber(0)
                .pageSize(5)
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
