package com.grupo10.medication_service.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFoundException_retorna404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Medicamento", "id", 1L);

        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("status", 404);
        assertThat(response.getBody()).containsEntry("error", "Not Found");
        assertThat(response.getBody().get("message").toString()).contains("Medicamento");
    }

    @Test
    void handleResourceNotFoundException_constructorSimple_retornaMensaje() {
        ResourceNotFoundException ex = new ResourceNotFoundException("No encontrado");

        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("message")).isEqualTo("No encontrado");
    }

    @Test
    void handleBusinessLogicException_retorna409() {
        BusinessLogicException ex = new BusinessLogicException("El medicamento ya está inactivo");

        ResponseEntity<Map<String, Object>> response = handler.handleBusinessLogicException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("status", 409);
        assertThat(response.getBody()).containsEntry("error", "Business Logic Error");
        assertThat(response.getBody().get("message")).isEqualTo("El medicamento ya está inactivo");
    }

    @Test
    void handleValidationException_retorna400() {
        ValidationException ex = new ValidationException("idPaciente", "no puede ser nulo");

        ResponseEntity<Map<String, Object>> response = handler.handleValidationException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("status", 400);
        assertThat(response.getBody()).containsEntry("error", "Validation Error");
        assertThat(response.getBody().get("message").toString()).contains("idPaciente");
    }

    @Test
    void handleGlobalException_retorna500() {
        Exception ex = new RuntimeException("Error inesperado");

        ResponseEntity<Map<String, Object>> response = handler.handleGlobalException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("status", 500);
        assertThat(response.getBody()).containsEntry("error", "Internal Server Error");
        assertThat(response.getBody().get("message")).isEqualTo("Ha ocurrido un error inesperado");
    }

    @Test
    void handleResourceNotFoundException_contieneTimestamp() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso", "id", 1L);

        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFoundException(ex);

        assertThat(response.getBody()).containsKey("timestamp");
        assertThat(response.getBody().get("timestamp")).isNotNull();
    }

    @Test
    void handleBusinessLogicException_contieneTimestamp() {
        BusinessLogicException ex = new BusinessLogicException("Error");

        ResponseEntity<Map<String, Object>> response = handler.handleBusinessLogicException(ex);

        assertThat(response.getBody()).containsKey("timestamp");
    }

    @Test
    void handleValidationException_contieneTimestamp() {
        ValidationException ex = new ValidationException("campo", "inválido");

        ResponseEntity<Map<String, Object>> response = handler.handleValidationException(ex);

        assertThat(response.getBody()).containsKey("timestamp");
    }
}