package com.ssf.immunizationrec.constant;

import com.ssf.immunizationrec.dtos.FilterDto;
import com.ssf.immunizationrec.dtos.ImmunizationRecDto;
import com.ssf.immunizationrec.dtos.RecommendationsDto;
import com.ssf.immunizationrec.dtos.VaccinesDto;
import com.ssf.immunizationrec.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String IMMUN_ID = "123-456-789-123";
    public static final String INVALID_IMMUN_ID = "123-789-123";
    public static final String CODE = "vac_code";
    public static final String REC_DES = "test_rec_des";
    public static final String REC_SERIES = "4231-456-789-1211";
    public static final  String CITIZEN_ID ="123-456-789-123";
    public static final  String PATIENT_ID ="124-456-789-124";
    public static final  String UHID ="124-421-789-124";


    public static VaccinesDto buildVaccinesDto() {
        return VaccinesDto.builder()
                .code(CODE)
                .earliestDueDate(LocalDate.now())
                .vaccinatedDate(LocalDate.now())
                .build();
    }
    public static RecommendationsDto buildRecommendationsDto() {
        return RecommendationsDto.builder()
                .description(REC_DES)
                .vaccines(List.of(buildVaccinesDto()))
                .series(REC_SERIES)
                .build();
    }

    public static ImmunizationRecDto buildImmunizationRecDto() {
        return ImmunizationRecDto.builder()
                .id(IMMUN_ID)
                .citizenId(CITIZEN_ID)
                .patientId(PATIENT_ID)
                .recommendedDate(LocalDate.of(2022,8,20))
                .recommendations(List.of(buildRecommendationsDto()))
                .build();
    }

    public  static FilterDto buildFullFilterDto() {
        return  FilterDto.builder()
                .citizenId(CITIZEN_ID)
                .patientId(PATIENT_ID)
                .uhid(UHID)
                .recommendedDate(LocalDate.of(2022,8,20))
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
