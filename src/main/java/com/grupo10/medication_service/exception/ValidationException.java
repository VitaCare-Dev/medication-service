package com.grupo10.medication_service.exception;

/**
 * Excepción para errores de validación de datos.
 * Se lanza cuando los datos de entrada no cumplen con los requisitos, como:
 * - Campos obligatorios vacíos o nulos
 * - Formatos de datos incorrectos
 * - Valores fuera de rango permitido
 * - Datos inconsistentes o inválidos
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String reason) {
        super(String.format("Error de validación en el campo '%s': %s", field, reason));
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
