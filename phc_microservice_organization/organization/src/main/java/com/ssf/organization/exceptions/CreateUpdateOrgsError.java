package com.ssf.organization.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateOrgsError extends Exception {

    private String message;

    public CreateUpdateOrgsError(Exception e) {
        if (e.getCause() != null) {
            if (e.getCause().getMessage() != null) {
                this.message = e.getCause().getMessage();
            }
        } else {
            this.message = e.getMessage();
        }
    }
}