package com.grupo10.medication_service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MedicationRequestDto {
    private Long idPaciente;
    private String nombreMedicamento;
    private String dosis;
    private int frecuenciaHoras;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private int activo;
}
