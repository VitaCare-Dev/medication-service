package com.grupo10.medication_service.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en la base de datos.
 *
 * <p>Mapeada por {@link GlobalExceptionHandler} a una respuesta HTTP {@code 404 Not Found}.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo personalizado.
     *
     * @param message descripción del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Crea la excepción con un mensaje formateado que indica qué recurso,
     * campo y valor no fueron encontrados.
     *
     * @param resource nombre del recurso (p. ej. "Medicamento")
     * @param field    nombre del campo de búsqueda (p. ej. "id")
     * @param value    valor buscado que no produjo resultado
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s no encontrado con %s: %s", resource, field, value));
    }
}
