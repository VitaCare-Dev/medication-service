package com.grupo10.medication_service.exception;

/**
 * Excepción para errores de lógica de negocio.
 * Se lanza cuando se viola una regla de negocio, como intentar:
 * - Desactivar un medicamento ya inactivo
 * - Marcar como tomado un medicamento que ya fue tomado
 * - Crear un tratamiento con fechas inválidas (inicio > fin)
 */
public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
