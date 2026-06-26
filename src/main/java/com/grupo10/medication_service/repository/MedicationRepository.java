package com.grupo10.medication_service.repository;

import com.grupo10.medication_service.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByIdPaciente(Long idPaciente);

    List<Medication> findByIdPacienteAndActivo(Long idPaciente, int activo);

}
