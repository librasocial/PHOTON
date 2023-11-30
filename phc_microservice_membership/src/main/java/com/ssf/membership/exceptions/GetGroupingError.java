package com.ssf.membership.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetGroupingError extends Exception {
    private String message;
}
