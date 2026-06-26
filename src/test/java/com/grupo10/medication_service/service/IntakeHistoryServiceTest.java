package com.grupo10.medication_service.service;

import com.grupo10.medication_service.dto.IntakeHistoryResponseDto;
import com.grupo10.medication_service.exception.BusinessLogicException;
import com.grupo10.medication_service.exception.ResourceNotFoundException;
import com.grupo10.medication_service.model.IntakeHistory;
import com.grupo10.medication_service.model.Medication;
import com.grupo10.medication_service.repository.IntakeHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntakeHistoryServiceTest {

    @Mock
    private IntakeHistoryRepository intakeHistoryRepository;

    @InjectMocks
    private IntakeHistoryService intakeHistoryService;

    private IntakeHistory intakeHistory;
    private Medication medication;

    @BeforeEach
    void setUp() {
        medication = new Medication();
        medication.setIdMedicamento(1L);
        medication.setNombreMedicamento("Paracetamol");

        intakeHistory = new IntakeHistory();
        intakeHistory.setId(1L);
        intakeHistory.setMedicamento(medication);
        intakeHistory.setFechaHora(LocalDateTime.of(2026, 1, 10, 8, 0));
        intakeHistory.setFechaHoraReal(null);
        intakeHistory.setTomado(false);
    }

    // --- getIntakeHistoryById ---

    @Test
    void getIntakeHistoryById_existente_retornaHistorial() {
        when(intakeHistoryRepository.findById(1L)).thenReturn(Optional.of(intakeHistory));

        IntakeHistoryResponseDto result = intakeHistoryService.getIntakeHistoryById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getIdMedicamento()).isEqualTo(1L);
        assertThat(result.getTomado()).isFalse();
    }

    @Test
    void getIntakeHistoryById_noExistente_lanzaResourceNotFoundException() {
        when(intakeHistoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> intakeHistoryService.getIntakeHistoryById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- getIntakeHistoriesByMedicationId ---

    @Test
    void getIntakeHistoriesByMedicationId_retornaLista() {
        when(intakeHistoryRepository.findByMedicamento_IdMedicamento(1L))
                .thenReturn(List.of(intakeHistory));

        List<IntakeHistoryResponseDto> result = intakeHistoryService.getIntakeHistoriesByMedicationId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdMedicamento()).isEqualTo(1L);
    }

    @Test
    void getIntakeHistoriesByMedicationId_sinHistorial_retornaListaVacia() {
        when(intakeHistoryRepository.findByMedicamento_IdMedicamento(99L)).thenReturn(List.of());

        List<IntakeHistoryResponseDto> result = intakeHistoryService.getIntakeHistoriesByMedicationId(99L);

        assertThat(result).isEmpty();
    }

    // --- getPendingIntakesByMedicationId ---

    @Test
    void getPendingIntakesByMedicationId_retornaPendientes() {
        when(intakeHistoryRepository
                .findByMedicamento_IdMedicamentoAndTomadoIsFalseOrMedicamento_IdMedicamentoAndTomadoIsNull(1L, 1L))
                .thenReturn(List.of(intakeHistory));

        List<IntakeHistoryResponseDto> result = intakeHistoryService.getPendingIntakesByMedicationId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTomado()).isFalse();
    }

    @Test
    void getPendingIntakesByMedicationId_sinPendientes_retornaListaVacia() {
        when(intakeHistoryRepository
                .findByMedicamento_IdMedicamentoAndTomadoIsFalseOrMedicamento_IdMedicamentoAndTomadoIsNull(1L, 1L))
                .thenReturn(List.of());

        List<IntakeHistoryResponseDto> result = intakeHistoryService.getPendingIntakesByMedicationId(1L);

        assertThat(result).isEmpty();
    }

    // --- markAsTaken ---

    @Test
    void markAsTaken_noTomado_marcaComoTomado() {
        intakeHistory.setTomado(false);
        IntakeHistory updated = new IntakeHistory();
        updated.setId(1L);
        updated.setMedicamento(medication);
        updated.setTomado(true);
        updated.setFechaHoraReal(LocalDateTime.now());

        when(intakeHistoryRepository.findById(1L)).thenReturn(Optional.of(intakeHistory));
        when(intakeHistoryRepository.save(any(IntakeHistory.class))).thenReturn(updated);

        IntakeHistoryResponseDto result = intakeHistoryService.markAsTaken(1L);

        assertThat(result.getTomado()).isTrue();
        assertThat(result.getFechaHoraReal()).isNotNull();
    }

    @Test
    void markAsTaken_tomadoNulo_marcaComoTomado() {
        intakeHistory.setTomado(null);
        IntakeHistory updated = new IntakeHistory();
        updated.setId(1L);
        updated.setMedicamento(medication);
        updated.setTomado(true);
        updated.setFechaHoraReal(LocalDateTime.now());

        when(intakeHistoryRepository.findById(1L)).thenReturn(Optional.of(intakeHistory));
        when(intakeHistoryRepository.save(any(IntakeHistory.class))).thenReturn(updated);

        IntakeHistoryResponseDto result = intakeHistoryService.markAsTaken(1L);

        assertThat(result.getTomado()).isTrue();
    }

    @Test
    void markAsTaken_yaFueTomado_lanzaBusinessLogicException() {
        intakeHistory.setTomado(true);
        when(intakeHistoryRepository.findById(1L)).thenReturn(Optional.of(intakeHistory));

        assertThatThrownBy(() -> intakeHistoryService.markAsTaken(1L))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("ya fue marcado como tomado");
    }

    @Test
    void markAsTaken_noExistente_lanzaResourceNotFoundException() {
        when(intakeHistoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> intakeHistoryService.markAsTaken(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- deleteIntakeHistory ---

    @Test
    void deleteIntakeHistory_existente_eliminaCorrectamente() {
        when(intakeHistoryRepository.existsById(1L)).thenReturn(true);

        intakeHistoryService.deleteIntakeHistory(1L);

        verify(intakeHistoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteIntakeHistory_noExistente_lanzaResourceNotFoundException() {
        when(intakeHistoryRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> intakeHistoryService.deleteIntakeHistory(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}