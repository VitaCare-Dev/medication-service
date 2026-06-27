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

/**
 * Manejador global de excepciones para todos los controladores REST.
 *
 * <p>Intercepta las excepciones del dominio y las convierte en respuestas HTTP
 * estructuradas con los campos: {@code timestamp}, {@code status}, {@code error}
 * y {@code message}. La marca temporal se genera con la zona horaria
 * {@value GlobalConstants#ZONE_ID}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja {@link ResourceNotFoundException} y devuelve {@code 404 Not Found}.
     *
     * @param ex excepción capturada
     * @return respuesta con detalle del recurso no encontrado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.NOT_FOUND.value());
        errorResponse.put(GlobalConstants.ERROR, "Not Found");
        errorResponse.put(GlobalConstants.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja {@link BusinessLogicException} y devuelve {@code 409 Conflict}.
     *
     * @param ex excepción capturada
     * @return respuesta con detalle de la violación de negocio
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessLogicException(BusinessLogicException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.CONFLICT.value());
        errorResponse.put(GlobalConstants.ERROR, "Business Logic Error");
        errorResponse.put(GlobalConstants.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja {@link ValidationException} y devuelve {@code 400 Bad Request}.
     *
     * @param ex excepción capturada
     * @return respuesta con detalle del error de validación
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(GlobalConstants.TIMESTAMP, LocalDateTime.now(ZoneId.of(GlobalConstants.ZONE_ID)));
        errorResponse.put(GlobalConstants.STATUS, HttpStatus.BAD_REQUEST.value());
        errorResponse.put(GlobalConstants.ERROR, "Validation Error");
        errorResponse.put(GlobalConstants.MESSAGE, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura cualquier excepción no controlada y devuelve {@code 500 Internal Server Error}.
     *
     * @param ex excepción capturada
     * @return respuesta genérica de error interno del servidor
     */
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
