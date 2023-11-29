package com.ssf.laborders.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EntityNotFoundException extends RuntimeException {
    private final String message;
}
