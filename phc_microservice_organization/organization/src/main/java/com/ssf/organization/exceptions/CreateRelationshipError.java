package com.ssf.organization.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRelationshipError extends Exception{
    private String message;

    public CreateRelationshipError(Exception e) {
        if (e.getCause() != null) {
            if (e.getCause().getMessage() != null) {
                this.message = e.getCause().getMessage();
            }
        } else {
            this.message = e.getMessage();
        }
    }
}
