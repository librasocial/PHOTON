package com.ssf.inward.constant;


import com.ssf.inward.dtos.*;
import com.ssf.inward.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String INWARD_ID = "123-456-789-123";
    public static final String PRODUCT_ID = "121-456-789-123";
    public static final  String PRODUCT_NAME ="test_product";
    public static final  String POTYPE ="PO";
    public static final String SUPPLIER_NAME ="SUPPLIER";
    public static final String STATUS ="CREATED";

    public static InwardDto buildInwardDto() {
        return InwardDto.builder()
                .id(INWARD_ID)
                .poDetails(buildPoDetails())
                .indentDetails(buildIndentDetailsDto())
                .indentItems(List.of(buildIndentItems()))
                .supplierName(SUPPLIER_NAME)
                .status("CREATED")
                .build();
    }

    private static PoDetailsDto buildPoDetails() {
        return PoDetailsDto.builder()
                .poId(PRODUCT_ID)
                .poDate(LocalDate.of(2022, Month.SEPTEMBER,8))
                .poAmt(11)
                .remarks("test")
                .build();
    }

    private static IndentDetailsDto buildIndentDetailsDto() {
        return IndentDetailsDto.builder().remarks("test").deliveryChallanNo("121").build();
    }

    private static IndentItemDto buildIndentItems() {
        return IndentItemDto.builder().productId(PRODUCT_ID).productName(PRODUCT_NAME).build();
    }
    public static ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }

}
