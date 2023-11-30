package com.ssf.pharmacyorder.constant;

import com.ssf.pharmacyorder.dtos.*;
import com.ssf.pharmacyorder.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String ORDER_ID = "123-456-789-123";
    public static final String PATIENT_ID = "121-456-789-123";
    public static final String PATIENT_NAME = "Mr.Nag";
    public static final String ENC_ID = "122-456-789-123";
    public static final String STAFF_ID = "126-456-789-123";
    public static final String STAFF_NAME = "Mr.Neil";
    public static final String PRODUCT_NAME = "dolo";
    public static final String PRODUCT_ID = "random-uuid";
    public static final String BATCH_NO = "121";
    public static final String DIS_ID = "124-456-789-123";
    public static final LocalDateTime ORDER_DATE = LocalDateTime.of(2020, 9, 12, 10, 12, 30);

    public static PatientDto buildPatientDto() {
        return PatientDto.builder().name(PATIENT_NAME)
                .patientId(PATIENT_ID)
                .build();
    }

    public static EncounterDto buildEncounterDto() {
        return EncounterDto.builder()
                .encounterId(ENC_ID)
                .staffId(STAFF_ID)
                .staffName(STAFF_NAME)
                .build();
    }

    public static ItemsDto buildItemsDto() {
        return ItemsDto.builder()
                .productId(PRODUCT_ID)
                .productName(PRODUCT_NAME)
                .build();
    }

    public static DispenseDto buildDispenseDto() {
        return DispenseDto.builder()
                .id(DIS_ID)
                .orderDate(ORDER_DATE)
                .orderId(ORDER_ID)
                .build();
    }


    public static PharmacyOrderDto buildPharmacyOrderDto() {
        return PharmacyOrderDto.builder()
                .id(ORDER_ID)
                .status(StatusEnum.ORDERED)
                .patient(buildPatientDto())
                .encounter(buildEncounterDto())
                .items(List.of(buildItemsDto()))
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
