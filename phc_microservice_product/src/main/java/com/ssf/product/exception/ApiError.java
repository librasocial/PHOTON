package com.ssf.product.exception;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiError {

    private String description;
    private List<ApiSubError> errors;
}
