package com.ssf.ancregistration.exception;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiSubError {
    private String errorCode;
    private String field;
    private String message;
}
