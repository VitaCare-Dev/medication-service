package com.grupo10.medication_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDate;

/**
 * Entidad JPA que representa un tratamiento farmacológico asignado a un paciente.
 *
 * <p>Se persiste en la tabla {@code tb_tratamiento_medicamento} y almacena
 * la información necesaria para gestionar el ciclo de vida de un medicamento:
 * nombre, dosis, frecuencia de administración, vigencia temporal y estado activo.
 */
@Data
@Entity
@Table(name = "tb_tratamiento_medicamento")
public class Medication {

    /**
     * Identificador único del tratamiento, generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamento")
    private Long idMedicamento;

    /**
     * Identificador del paciente al que pertenece este tratamiento.
     */
    @Column(name = "id_paciente")
    private Long idPaciente;

    /**
     * Nombre comercial o genérico del medicamento.
     */
    @Column(name = "nombre_medicamento")
    private String nombreMedicamento;

    /**
     * Descripción de la dosis a administrar (p. ej. "500 mg", "1 comprimido").
     */
    @Column(name = "dosis")
    private String dosis;

    /**
     * Intervalo de administración expresado en horas (p. ej. {@code 8} para cada 8 horas).
     */
    @Column(name = "frecuencia_horas")
    private int frecuenciaHoras;

    /**
     * Fecha en que inicia el tratamiento.
     */
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    /**
     * Fecha en que finaliza el tratamiento. Puede ser {@code null} si el tratamiento es indefinido.
     */
    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    /**
     * Indicador de estado del tratamiento: {@code 1} activo, {@code 0} inactivo.
     */
    @Column(name = "activo")
    private int activo;

}
