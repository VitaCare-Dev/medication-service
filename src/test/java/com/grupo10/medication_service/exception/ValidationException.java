package com.grupo10.medication_service.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValidationExceptionTest {

    @Test
    void testConstructorWithMessage() {

        String expectedMessage = "Los datos del paciente están incompletos";


        ValidationException exception = new ValidationException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause()); 
    }

    @Test
    void testConstructorWithFieldAndReason() {
        String field = "frecuenciaHoras";
        String reason = "debe ser mayor a 0";
        String expectedMessage = "Error de validación en el campo 'frecuenciaHoras': debe ser mayor a 0";

        ValidationException exception = new ValidationException(field, reason);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String expectedMessage = "Fallo al validar la dosis";
        Throwable expectedCause = new NumberFormatException("Formato de número inválido");

        ValidationException exception = new ValidationException(expectedMessage, expectedCause);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}