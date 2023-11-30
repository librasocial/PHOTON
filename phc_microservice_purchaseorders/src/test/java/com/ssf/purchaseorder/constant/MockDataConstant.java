package com.ssf.purchaseorder.constant;

import com.ssf.purchaseorder.dtos.IndentItemDto;
import com.ssf.purchaseorder.dtos.PoDetailsDto;
import com.ssf.purchaseorder.dtos.PoItemDto;
import com.ssf.purchaseorder.dtos.PurchaseOrderDto;
import com.ssf.purchaseorder.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String PURCHASE_ORDER_ID = "123-456-789-123";
    public static final String PRODUCT_ID = "121-456-789-123";
    public static final  String PRODUCT_NAME ="test_product";
    public static final  String POTYPE ="PURCAHSE";
    public static final String SUPPLIER_NAME ="SUPPLIER";

    public static PurchaseOrderDto buildPurchaseOrderDto() {
        return PurchaseOrderDto.builder()
                .id(PURCHASE_ORDER_ID)
                .indentItems(buildIndentItems())
                .poDetails(buildPoDetails())
                .status(StatusEnum.DRAFT)
                .supplierName(SUPPLIER_NAME)
                .type(POTYPE)
                .poDate(LocalDateTime.of(2022, Month.SEPTEMBER,8,10,10,10,100))
                .build();
    }

    private static PoDetailsDto buildPoDetails() {
        return PoDetailsDto.builder()
                .netAmount(10)
                .poItems( List.of(PoItemDto.builder()
                .productId(PRODUCT_ID)
                .productName(PRODUCT_NAME).build())).build();
    }

    private static List<IndentItemDto> buildIndentItems() {
        return List.of(IndentItemDto.builder().productId(PRODUCT_ID).productName(PRODUCT_NAME).build());
    }


    public static ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }

}
