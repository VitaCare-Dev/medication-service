package com.grupo10.medication_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.grupo10.medication_service.constants.GlobalConstants;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import java.time.ZoneId;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.NOT_FOUND.value());
        errorResponse.put(GlobalConstants.ERROR, "Not Found");
        errorResponse.put(GlobalConstants.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessLogicException(BusinessLogicException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.CONFLICT.value());
        errorResponse.put(GlobalConstants.ERROR, "Business Logic Error");
        errorResponse.put(GlobalConstants.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.BAD_REQUEST.value());
        errorResponse.put(GlobalConstants.ERROR, "Validation Error");
        errorResponse.put(GlobalConstants.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put(GlobalConstants.ERROR, "Internal Server Error");
        errorResponse.put(GlobalConstants.MESSAGE, "Ha ocurrido un error inesperado");

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
