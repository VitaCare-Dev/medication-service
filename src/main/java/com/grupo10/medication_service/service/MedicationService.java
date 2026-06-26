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

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Transactional
    public MedicationResponseDto createTreatment(MedicationRequestDto requestDto) {
        // Validaciones
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

    @Transactional(readOnly = true)
    public MedicationResponseDto getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalConstants.MEDICATION, "id", id));
        return mapToResponseDto(medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDto> getMedicationsByPatientId(Long idPaciente) {
        return medicationRepository.findByIdPaciente(idPaciente).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDto> getActiveMedicationsByPatientId(Long idPaciente) {
        return medicationRepository.findByIdPacienteAndActivo(idPaciente, 1).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new ResourceNotFoundException(GlobalConstants.MEDICATION, "id", id);
        }
        medicationRepository.deleteById(id);
    }

    @Transactional
    public MedicationResponseDto deactivateMedication(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalConstants.MEDICATION, "id", id));

        // Validación de lógica de negocio
        if (medication.getActivo() == 0) {
            throw new BusinessLogicException("El medicamento con ID " + id + " ya está inactivo");
        }

        medication.setActivo(0);
        Medication updatedMedication = medicationRepository.save(medication);
        return mapToResponseDto(updatedMedication);
    }

    /**
     * Valida los datos de entrada para crear o actualizar un medicamento
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

        // Validar que fecha de término sea posterior a fecha de inicio
        if (requestDto.getFechaTermino() != null &&
                requestDto.getFechaTermino().isBefore(requestDto.getFechaInicio())) {
            throw new BusinessLogicException("La fecha de término no puede ser anterior a la fecha de inicio");
        }
    }

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
