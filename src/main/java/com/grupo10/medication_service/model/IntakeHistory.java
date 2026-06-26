package com.grupo10.medication_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_historial_toma")
public class IntakeHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_toma")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medication medicamento;

    @Column(name = "fecha_hora_programada")
    private LocalDateTime fechaHora;

    @Column(name = "fecha_hora_real")
    private LocalDateTime fechaHoraReal;

    @Column(name = "tomado")
    private Boolean tomado;

}
