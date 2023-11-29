package com.ssf.surveycube.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MissmatchException extends RuntimeException {
    private final String message;
}
