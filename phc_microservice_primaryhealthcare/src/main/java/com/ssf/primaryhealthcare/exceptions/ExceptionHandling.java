package com.ssf.primaryhealthcare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CreateRelationshipError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> handleConflict(CreateRelationshipError e, HttpServletRequest request) {

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", e.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.internalServerError().body(response);
    }

}
