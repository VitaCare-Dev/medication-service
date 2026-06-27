package com.grupo10.medication_service.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO de entrada para la creación de un tratamiento farmacológico.
 *
 * <p>Encapsula los datos enviados por el cliente al registrar un nuevo medicamento
 * para un paciente. No incluye el identificador del tratamiento porque éste
 * es generado por la capa de persistencia.
 */
@Data
public class MedicationRequestDto {

    /**
     * Identificador del paciente al que se asignará el tratamiento.
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
     * Estado inicial del tratamiento: {@code 1} activo, {@code 0} inactivo.
     */
    private int activo;
}
