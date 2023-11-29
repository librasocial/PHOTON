package com.ssf.primaryhealthcare.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRelationshipError extends Exception{
    private String message;
}
