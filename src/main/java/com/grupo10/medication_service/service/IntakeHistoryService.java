package com.grupo10.medication_service.service;

import com.grupo10.medication_service.dto.IntakeHistoryResponseDto;
import com.grupo10.medication_service.exception.BusinessLogicException;
import com.grupo10.medication_service.exception.ResourceNotFoundException;
import com.grupo10.medication_service.model.IntakeHistory;
import com.grupo10.medication_service.repository.IntakeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.grupo10.medication_service.constants.GlobalConstants;

@Service
@RequiredArgsConstructor
public class IntakeHistoryService {

    private final IntakeHistoryRepository intakeHistoryRepository;

    @Transactional(readOnly = true)
    public IntakeHistoryResponseDto getIntakeHistoryById(Long id) {
        IntakeHistory intakeHistory = intakeHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GlobalConstants.HISTORY, "id", id));
        return mapToResponseDto(intakeHistory);
    }

    @Transactional(readOnly = true)
    public List<IntakeHistoryResponseDto> getIntakeHistoriesByMedicationId(Long idMedicamento) {
        return intakeHistoryRepository.findByMedicamento_IdMedicamento(idMedicamento).stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IntakeHistoryResponseDto> getPendingIntakesByMedicationId(Long idMedicamento) {
        return intakeHistoryRepository
                .findByMedicamento_IdMedicamentoAndTomadoIsFalseOrMedicamento_IdMedicamentoAndTomadoIsNull(
                        idMedicamento, idMedicamento)
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Transactional
    public IntakeHistoryResponseDto markAsTaken(Long id) {
        IntakeHistory intakeHistory = intakeHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial de toma", "id", id));

        // Validación de lógica de negocio
        if (Boolean.TRUE.equals(intakeHistory.getTomado())) {
            throw new BusinessLogicException("El medicamento con ID " + id + " ya fue marcado como tomado");
        }

        intakeHistory.setTomado(true);
        intakeHistory.setFechaHoraReal(LocalDateTime.now(ZoneId.of("America/Santiago")));

        IntakeHistory updatedIntakeHistory = intakeHistoryRepository.save(intakeHistory);
        return mapToResponseDto(updatedIntakeHistory);
    }

    @Transactional
    public void deleteIntakeHistory(Long id) {
        if (!intakeHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historial de toma", "id", id);
        }
        intakeHistoryRepository.deleteById(id);
    }

    private IntakeHistoryResponseDto mapToResponseDto(IntakeHistory intakeHistory) {
        IntakeHistoryResponseDto responseDto = new IntakeHistoryResponseDto();
        responseDto.setId(intakeHistory.getId());
        responseDto.setIdMedicamento(intakeHistory.getMedicamento().getIdMedicamento());
        responseDto.setFechaHora(intakeHistory.getFechaHora());
        responseDto.setFechaHoraReal(intakeHistory.getFechaHoraReal());
        responseDto.setTomado(intakeHistory.getTomado());
        return responseDto;
    }
}
