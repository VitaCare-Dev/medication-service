package com.grupo10.medication_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "tb_tratamiento_medicamento")
public class Medication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamento")
    private Long idMedicamento;

    @Column(name = "id_paciente")
    private Long idPaciente;

    @Column(name = "nombre_medicamento")
    private String nombreMedicamento;

    @Column(name = "dosis")
    private String dosis;

    @Column(name = "frecuencia_horas")
    private int frecuenciaHoras;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    @Column(name = "activo")
    private int activo;

    
}
