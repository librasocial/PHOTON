package com.ssf.product.constant;

import com.ssf.product.dtos.InventoryDto;
import com.ssf.product.dtos.ProductDto;
import com.ssf.product.dtos.UomDto;
import com.ssf.product.exception.ApiSubError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MockDataConstant {
    public static final String PRODUCT_ID = "123-456-789-123";
    public static final String INVENTORY_ID = "124-456-789-123";
    public static final  String PRODUCT_NAME ="test_product";
    public static final  String BATCH_NO ="123";

    public static InventoryDto buildInventoryDto() {
        return InventoryDto.builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .status(InventoryStatusEnum.INSTOCK)
                .batchNumber(BATCH_NO)
                .build();
    }
    public static ProductDto buildProductDto() {
        return ProductDto.builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .umo(List.of(UomDto.builder().alternateUOM("test").build()))
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
