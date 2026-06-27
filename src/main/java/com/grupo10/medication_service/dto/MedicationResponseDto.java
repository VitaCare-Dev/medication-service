package com.grupo10.medication_service.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO de salida que representa la información de un tratamiento farmacológico
 * devuelta al cliente tras una operación exitosa.
 *
 * <p>A diferencia de {@link MedicationRequestDto}, incluye el identificador del
 * tratamiento ({@code idMedicamento}) asignado por la base de datos.
 */
@Data
public class MedicationResponseDto {

    /**
     * Identificador único del tratamiento generado por la base de datos.
     */
    private Long idMedicamento;

    /**
     * Identificador del paciente al que pertenece el tratamiento.
     */
    private Long idPaciente;

    /**
     * Nombre comercial o genérico del medicamento.
     */
    private String nombreMedicamento;

    /**
     * Descripción de la dosis a administrar (p. ej. "500 mg", "1 comprimido").
     */
    private String dosis;

    /**
     * Intervalo de administración expresado en horas.
     */
    private int frecuenciaHoras;

    /**
     * Fecha de inicio del tratamiento.
     */
    private LocalDate fechaInicio;

    /**
     * Fecha de término del tratamiento. Puede ser {@code null} si el tratamiento es indefinido.
     */
    private LocalDate fechaTermino;

    /**
     * Estado del tratamiento: {@code 1} activo, {@code 0} inactivo.
     */
    private int activo;
}
