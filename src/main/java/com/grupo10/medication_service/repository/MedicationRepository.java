package com.grupo10.medication_service.repository;

import com.grupo10.medication_service.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Medication}.
 *
 * <p>Extiende {@link JpaRepository} y hereda las operaciones CRUD estándar.
 * Adicionalmente expone consultas derivadas por convención de nombres de Spring Data JPA
 * para filtrar tratamientos por paciente y estado.
 */
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    /**
     * Recupera todos los tratamientos registrados para un paciente dado,
     * independientemente de su estado activo o inactivo.
     *
     * @param idPaciente identificador del paciente
     * @return lista de tratamientos asociados al paciente; vacía si no existen registros
     */
    List<Medication> findByIdPaciente(Long idPaciente);

    /**
     * Recupera los tratamientos de un paciente filtrados por estado activo.
     *
     * @param idPaciente identificador del paciente
     * @param activo     valor de estado: {@code 1} para activos, {@code 0} para inactivos
     * @return lista de tratamientos que cumplen ambos criterios; vacía si no existen registros
     */
    List<Medication> findByIdPacienteAndActivo(Long idPaciente, int activo);

}
