package com.grupo10.medication_service.repository;

import com.grupo10.medication_service.model.IntakeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntakeHistoryRepository extends JpaRepository<IntakeHistory, Long> {

    // Derived Query Method: busca por la propiedad idMedicamento dentro de la
    // relación medicamento
    List<IntakeHistory> findByMedicamento_IdMedicamento(Long idMedicamento);

    // Derived Query Method: busca por medicamento donde tomado es false O es null
    // Spring Data JPA requiere pasar el idMedicamento dos veces debido al OR
    List<IntakeHistory> findByMedicamento_IdMedicamentoAndTomadoIsFalseOrMedicamento_IdMedicamentoAndTomadoIsNull(
            Long idMedicamento1, Long idMedicamento2);

}
