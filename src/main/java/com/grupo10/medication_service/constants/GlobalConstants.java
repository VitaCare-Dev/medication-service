package com.grupo10.medication_service.constants;

import lombok.experimental.UtilityClass;

/**
 * Constantes globales compartidas entre las capas del microservicio.
 *
 * <p>Centraliza los literales de cadena usados en respuestas de error,
 * nombres de campos JSON y configuración de zona horaria, evitando
 * cadenas duplicadas a lo largo del código.
 */
@UtilityClass
public class GlobalConstants {

    /** Nombre del recurso de dominio principal utilizado en mensajes de error. */
    public static final String MEDICATION = "Medicamento";

    /** Clave JSON para la marca temporal en respuestas de error. */
    public static final String TIMESTAMP = "timestamp";

    /** Clave JSON para el código de estado HTTP en respuestas de error. */
    public static final String STATUS = "status";

    /** Clave JSON para la descripción corta del error en respuestas de error. */
    public static final String ERROR = "error";

    /** Clave JSON para el mensaje descriptivo en respuestas de error. */
    public static final String MESSAGE = "message";

    /** Identificador de zona horaria utilizado para las marcas temporales de error. */
    public static final String ZONE_ID = "America/Santiago";

}
