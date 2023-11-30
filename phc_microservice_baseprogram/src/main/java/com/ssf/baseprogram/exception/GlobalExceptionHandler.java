package com.ssf.baseprogram.exception;

import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        log.error("Resource not found exception {}  ", ex);

        ApiSubError subError = buildApiSubError(ex.getMessage(), String.valueOf(NOT_FOUND.value()), null);
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        ApiError apiError = ApiError.builder()
                .description("Resource is not available")
                .errors(errorList).build();
        return buildResponseEntity(apiError, NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
        log.error("ConstraintViolation  exception {}  ", ex);
        // Get the error messages for invalid fields
        List<ApiSubError> errorList = ex.getConstraintViolations()
                .stream()
                .map(fe -> buildApiSubError(fe.getMessage(), String.valueOf(BAD_REQUEST.value()), fe.getPropertyPath().toString()))
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .description("Invalid input passed in request body")
                .errors(errorList).build();
        return buildResponseEntity(apiError, BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        logger.error("Invalid arguments found {} : " + ex);
        // Get the error messages for invalid fields
        List<ApiSubError> errorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> buildApiSubError(fe.getDefaultMessage(), String.valueOf(BAD_REQUEST.value()), fe.getField()))
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()
                .description("Invalid input passed in request body")
                .errors(errorList).build();
        return buildResponseEntity(apiError, BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Request Body contain invalid Json Format", ex.getMessage());

        ApiSubError subError = buildApiSubError(ex.getMessage(), String.valueOf(BAD_REQUEST.value()), null);
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        ApiError apiError = ApiError.builder()
                .description("Request Body contain invalid Json Format")
                .errors(errorList).build();
        return buildResponseEntity(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleException(RuntimeException ex) {
        log.error("Internal Server Error {} ", ex);

        ApiSubError subError = buildApiSubError(ex.getMessage(), String.valueOf(INTERNAL_SERVER_ERROR.value()), null);
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        ApiError apiError = ApiError.builder()
                .description("Internal Server Error")
                .errors(errorList).build();
        return buildResponseEntity(apiError, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Object> handleAuthroizationExcpetion(RuntimeException ex) {
        log.error("Jwt Identity token is not valid {} ", ex);

        ApiSubError subError = buildApiSubError(ex.getMessage(), String.valueOf(UNAUTHORIZED.value()), null);
        List<ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        ApiError apiError = ApiError.builder()
                .description("UNAUTHORIZED")
                .errors(errorList).build();
        return buildResponseEntity(apiError, UNAUTHORIZED);
    }

    private ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
