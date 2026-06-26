package com.grupo10.medication_service.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessLogicExceptionTest {

    @Test
    void testConstructorWithMessageAndCause() {
        String expectedMessage = "Ocurrió un error en la validación";
        Throwable expectedCause = new IllegalArgumentException("Falta el ID del paciente");

        BusinessLogicException exception = new BusinessLogicException(expectedMessage, expectedCause);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
    }
}
