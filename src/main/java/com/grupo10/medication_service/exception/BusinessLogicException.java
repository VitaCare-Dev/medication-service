package com.grupo10.medication_service.exception;

/**
 * Excepción lanzada cuando se viola una regla de negocio del dominio.
 *
 * <p>Ejemplos de situaciones que la originan:
 * <ul>
 *   <li>Intentar desactivar un medicamento que ya está inactivo.</li>
 *   <li>Registrar un tratamiento cuya fecha de término es anterior a la de inicio.</li>
 * </ul>
 *
 * <p>Mapeada por {@link GlobalExceptionHandler} a una respuesta HTTP {@code 409 Conflict}.
 */
public class BusinessLogicException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo de la regla violada.
     *
     * @param message descripción de la violación de negocio
     */
    public BusinessLogicException(String message) {
        super(message);
    }

    /**
     * Crea la excepción con un mensaje descriptivo y la causa raíz del error.
     *
     * @param message descripción de la violación de negocio
     * @param cause   excepción original que originó este error
     */
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
