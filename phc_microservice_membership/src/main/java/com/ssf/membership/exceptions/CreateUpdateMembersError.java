package com.ssf.membership.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateMembersError extends Exception {

    private String message;
    public CreateUpdateMembersError(Exception e) {
        if (e.getCause() != null) {
            if (e.getCause().getMessage() != null) {
                this.message = e.getCause().getMessage();
            }
        } else {
            this.message = e.getMessage();
        }
    }
}
