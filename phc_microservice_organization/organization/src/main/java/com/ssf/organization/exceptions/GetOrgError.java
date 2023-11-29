package com.ssf.organization.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetOrgError extends RuntimeException {
    private String message;

    public GetOrgError(Exception e) {
        if (e.getCause() != null) {
            if (e.getCause().getMessage() != null) {
                this.message = e.getCause().getMessage();
            }
        } else {
            this.message = e.getMessage();
        }
    }
}
