package com.grupo10.medication_service.service;

import com.grupo10.medication_service.dto.MedicationRequestDto;
import com.grupo10.medication_service.dto.MedicationResponseDto;
import com.grupo10.medication_service.exception.BusinessLogicException;
import com.grupo10.medication_service.exception.ResourceNotFoundException;
import com.grupo10.medication_service.exception.ValidationException;
import com.grupo10.medication_service.model.Medication;
import com.grupo10.medication_service.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo10.medication_service.constants.GlobalConstants;

import java.util.List;

/**
 * Servicio de negocio para la gestión de tratamientos farmacológicos.
 *
 * <p>Centraliza la lógica de validación, creación, consulta, desactivación y
 * eliminación de medicamentos. Delega la persistencia en {@link MedicationRepository}
 * y aplica reglas de negocio antes de cada operación de escritura.
 */
@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    /**
     * Crea y persiste un nuevo tratamiento farmacológico.
     *
     * <p>Valida los datos de entrada antes de guardar. Si algún campo
     * obligatorio falta o contiene un valor inválido se lanza una excepción
     * antes de acceder a la base de datos.
     *
     * @param requestDto datos del tratamiento a registrar
     * @return representación del tratamiento recién creado, incluyendo el ID asignado
     * @throws ValidationException     si algún campo obligatorio es nulo, vacío o tiene valor inválido
     * @throws BusinessLogicException  si la fecha de término es anterior a la fecha de inicio
     */
    @Transactional
    public MedicationResponseDto createTreatment(MedicationRequestDto requestDto) {
        validateMedicationRequest(requestDto);

        Medication medication = new Medication();
        medication.setIdPaciente(requestDto.getIdPaciente());
        medication.setNombreMedicamento(requestDto.getNombreMedicamento());
        medication.setDosis(requestDto.getDosis());
        medication.setFrecuenciaHoras(requestDto.getFrecuenciaHoras());
        medication.setFechaInicio(requestDto.getFechaInicio());
        medication.setFechaTermino(requestDto.getFechaTermino());
        medication.setActivo(requestDto.getActivo());

        Medication savedMedication = medicationRepository.save(medication);
        return mapToResponseDto(savedMedication);
    }

    /**
     * Obtiene un tratamiento por su identificador único.
     *
     * @param id identificador del tratamiento
     * @return datos del tratamiento encontrado
     * @throws ResourceNotFoundException si no existe un tratamiento con el ID indicado
     */
    @Transactional(readOnly = true)
    public MedicationResponseDto getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalConstants.MEDICATION, "id", id));
        return mapToResponseDto(medication);
    }

    /**
     * Obtiene todos los tratamientos registrados para un paciente,
     * sin filtrar por estado activo o inactivo.
     *
     * @param idPaciente identificador del paciente
     * @return lista de tratamientos del paciente; vacía si no tiene registros
     */
    @Transactional(readOnly = true)
    public List<MedicationResponseDto> getMedicationsByPatientId(Long idPaciente) {
        return medicationRepository.findByIdPaciente(idPaciente).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    /**
     * Obtiene únicamente los tratamientos activos de un paciente.
     *
     * @param idPaciente identificador del paciente
     * @return lista de tratamientos con estado {@code activo = 1}; vacía si no existen
     */
    @Transactional(readOnly = true)
    public List<MedicationResponseDto> getActiveMedicationsByPatientId(Long idPaciente) {
        return medicationRepository.findByIdPacienteAndActivo(idPaciente, 1).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    /**
     * Elimina permanentemente un tratamiento de la base de datos.
     *
     * @param id identificador del tratamiento a eliminar
     * @throws ResourceNotFoundException si no existe un tratamiento con el ID indicado
     */
    @Transactional
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new ResourceNotFoundException(GlobalConstants.MEDICATION, "id", id);
        }
        medicationRepository.deleteById(id);
    }

    /**
     * Desactiva un tratamiento estableciendo su estado a {@code 0}.
     *
     * <p>No elimina el registro; permite mantener el historial del tratamiento.
     *
     * @param id identificador del tratamiento a desactivar
     * @return datos actualizados del tratamiento con estado {@code activo = 0}
     * @throws ResourceNotFoundException si no existe un tratamiento con el ID indicado
     * @throws BusinessLogicException    si el tratamiento ya se encuentra inactivo
     */
    @Transactional
    public MedicationResponseDto deactivateMedication(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalConstants.MEDICATION, "id", id));

        if (medication.getActivo() == 0) {
            throw new BusinessLogicException("El medicamento con ID " + id + " ya está inactivo");
        }

        medication.setActivo(0);
        Medication updatedMedication = medicationRepository.save(medication);
        return mapToResponseDto(updatedMedication);
    }

    /**
     * Valida los campos obligatorios y las reglas de negocio del DTO de entrada.
     *
     * @param requestDto datos a validar
     * @throws ValidationException    si un campo obligatorio es nulo, vacío o tiene valor inválido
     * @throws BusinessLogicException si la fecha de término es anterior a la fecha de inicio
     */
    private void validateMedicationRequest(MedicationRequestDto requestDto) {
        if (requestDto.getIdPaciente() == null) {
            throw new ValidationException("idPaciente", "no puede ser nulo");
        }

        if (requestDto.getNombreMedicamento() == null || requestDto.getNombreMedicamento().trim().isEmpty()) {
            throw new ValidationException("nombreMedicamento", "no puede estar vacío");
        }

        if (requestDto.getDosis() == null || requestDto.getDosis().trim().isEmpty()) {
            throw new ValidationException("dosis", "no puede estar vacía");
        }

        if (requestDto.getFrecuenciaHoras() <= 0) {
            throw new ValidationException("frecuenciaHoras", "debe ser mayor que 0");
        }

        if (requestDto.getFechaInicio() == null) {
            throw new ValidationException("fechaInicio", "no puede ser nula");
        }

        if (requestDto.getFechaTermino() != null &&
                requestDto.getFechaTermino().isBefore(requestDto.getFechaInicio())) {
            throw new BusinessLogicException("La fecha de término no puede ser anterior a la fecha de inicio");
        }
    }

    /**
     * Convierte una entidad {@link Medication} en su representación DTO de respuesta.
     *
     * @param medication entidad a convertir
     * @return DTO con los datos del tratamiento
     */
    private MedicationResponseDto mapToResponseDto(Medication medication) {
        MedicationResponseDto responseDto = new MedicationResponseDto();
        responseDto.setIdMedicamento(medication.getIdMedicamento());
        responseDto.setIdPaciente(medication.getIdPaciente());
        responseDto.setNombreMedicamento(medication.getNombreMedicamento());
        responseDto.setDosis(medication.getDosis());
        responseDto.setFrecuenciaHoras(medication.getFrecuenciaHoras());
        responseDto.setFechaInicio(medication.getFechaInicio());
        responseDto.setFechaTermino(medication.getFechaTermino());
        responseDto.setActivo(medication.getActivo());
        return responseDto;
    }
}
