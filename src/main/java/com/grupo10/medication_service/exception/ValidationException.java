package com.grupo10.medication_service.exception;

/**
 * Excepción lanzada cuando los datos de entrada no superan la validación del dominio.
 *
 * <p>Ejemplos de situaciones que la originan:
 * <ul>
 *   <li>Campos obligatorios nulos o vacíos.</li>
 *   <li>Valores numéricos fuera del rango permitido (p. ej. {@code frecuenciaHoras <= 0}).</li>
 *   <li>Datos en formato incorrecto o inconsistente.</li>
 * </ul>
 *
 * <p>Mapeada por {@link GlobalExceptionHandler} a una respuesta HTTP {@code 400 Bad Request}.
 */
public class ValidationException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo del error de validación.
     *
     * @param message descripción del error
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Crea la excepción con un mensaje formateado que identifica el campo
     * inválido y el motivo del rechazo.
     *
     * @param field  nombre del campo que falló la validación
     * @param reason explicación del motivo del fallo
     */
    public ValidationException(String field, String reason) {
        super(String.format("Error de validación en el campo '%s': %s", field, reason));
    }

    /**
     * Crea la excepción con un mensaje descriptivo y la causa raíz del error.
     *
     * @param message descripción del error de validación
     * @param cause   excepción original que originó este error
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
