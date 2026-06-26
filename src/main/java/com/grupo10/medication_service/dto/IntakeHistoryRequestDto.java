package com.grupo10.medication_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IntakeHistoryRequestDto {
    private Long idMedicamento;
    private LocalDateTime fechaHora;
    private LocalDateTime fechaHoraReal;
    private Boolean tomado;
}
