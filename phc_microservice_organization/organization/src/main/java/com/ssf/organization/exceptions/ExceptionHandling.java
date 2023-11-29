package com.ssf.organization.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@RestControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {CreateUpdateOrgsError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> handleConflict(CreateUpdateOrgsError e) {

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(value = {CreateRelationshipError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> handleConflict(CreateRelationshipError e) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(value = {GetOrgError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> handleConflict(GetOrgError e) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    /*
     * @ExceptionHandler(MissingServletRequestParameterException.class) public void
     * handleMissingParams(MissingServletRequestParameterException ex) { String name
     * = ex.getParameterName(); System.out.println(name + " parameter is missing");
     * // Actual exception handling }
     */

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        String name = ex.getParameterName();
        logger.error(name + " parameter is missing");

        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = null;
        if ("rel".equals(ex.getName())) {
            error = "Rel parameter is Incorrect. It should either one of these 'CONTAINEDINPLACE,SERVICEDAREA,RESIDESIN,MEMBEROF,ADMINISTEREDBY,SUBORGOF'";
        }
        if ("targetType".equals(ex.getName())) {
            error = "Target Type parameter is Incorrect. It should either one of these 'Phc,Country,District,GramPanchayat,HouseHold,State,SubCenter,Taluk,Village'";
        }
        if ("type".equals(ex.getName())) {
            error = "Type parameter is Incorrect. It should either one of these 'Phc,Country,District,GramPanchayat,HouseHold,State,SubCenter,Taluk,Village'";
        }


        OrgError apiError =
                new OrgError(HttpStatus.BAD_REQUEST, error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
