package com.ssf.primaryhealthcare.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetError extends Exception {
    private String message;
}
