package com.ssf.laborders.exception;

import com.ssf.laborders.constants.ValidatorConstants;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            com.ssf.laborders.exception.EntityNotFoundException ex) {
        log.error("Resource not found exception {} ", ex.getMessage());

        com.ssf.laborders.exception.ApiSubError subError = buildApiSubError(ex.getMessage(), NOT_FOUND.name(), null);
        List<com.ssf.laborders.exception.ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        com.ssf.laborders.exception.ApiError apiError = com.ssf.laborders.exception.ApiError.builder()
                .description("Resource is not available")
                .errors(errorList).build();
        return buildResponseEntity(apiError, NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        logger.error("Invalid arguments found : {}" + ex.getMessage());
        // Get the error messages for invalid fields
        List<com.ssf.laborders.exception.ApiSubError> errorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> buildApiSubError(fe.getDefaultMessage(), BAD_REQUEST.name(), fe.getField()))
                .collect(Collectors.toList());

        com.ssf.laborders.exception.ApiError apiError = com.ssf.laborders.exception.ApiError.builder()
                .description("Invalid input passed in request body")
                .errors(errorList).build();
        return buildResponseEntity(apiError, BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Request Body contain invalid Json Format: {}", ex.getMessage());

        com.ssf.laborders.exception.ApiSubError subError = buildApiSubError(ex.getMessage(), BAD_REQUEST.name(), null);
        List<com.ssf.laborders.exception.ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        com.ssf.laborders.exception.ApiError apiError = com.ssf.laborders.exception.ApiError.builder()
                .description("Request Body contain invalid Json Format")
                .errors(errorList).build();
        return buildResponseEntity(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleException(RuntimeException ex) {
        log.error("Internal Server Error {}", ex.getMessage());

        com.ssf.laborders.exception.ApiSubError subError = buildApiSubError(ex.getMessage(), INTERNAL_SERVER_ERROR.name(), null);
        List<com.ssf.laborders.exception.ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        com.ssf.laborders.exception.ApiError apiError = com.ssf.laborders.exception.ApiError.builder()
                .description("Bad Request")
                .errors(errorList).build();
        return buildResponseEntity(apiError, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LabOrdersException.class)
    public ResponseEntity<Object> handlelabordersException(LabOrdersException ex) {
        log.error("Lab Orders exception {}", ex.getMessage());

        com.ssf.laborders.exception.ApiSubError subError = buildApiSubError(ex.getMessage(), BAD_REQUEST.name(), null);
        List<com.ssf.laborders.exception.ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        com.ssf.laborders.exception.ApiError apiError = com.ssf.laborders.exception.ApiError.builder()
                .description("Bad Request")
                .errors(errorList).build();
        return buildResponseEntity(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Object> handleAuthroizationExcpetion(RuntimeException ex) {
        log.error("Jwt Identity token is not valid {}", ex.getMessage());

        com.ssf.laborders.exception.ApiSubError subError = buildApiSubError(ex.getMessage(), UNAUTHORIZED.name(), null);
        List<com.ssf.laborders.exception.ApiSubError> errorList = new ArrayList<>();
        errorList.add(subError);

        ApiError apiError = ApiError.builder()
                .description("UNAUTHORIZED")
                .errors(errorList).build();
        return buildResponseEntity(apiError, UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<com.ssf.laborders.exception.ApiSubError> errorList = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            com.ssf.laborders.exception.ApiSubError subError = buildApiSubError(violation.getMessage(), BAD_REQUEST.name(), null);
            errorList.add(subError);
        }
        com.ssf.laborders.exception.ApiError apiError = com.ssf.laborders.exception.ApiError.builder()
                .description(ValidatorConstants.REQUEST_VALIDATION_FAILED)
                .errors(errorList).build();

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private ApiSubError buildApiSubError(String message, String errorCode, String field) {
        return com.ssf.laborders.exception.ApiSubError.builder()
                .errorCode(errorCode)
                .message(message)
                .field(field)
                .build();
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
