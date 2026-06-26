package com.grupo10.medication_service.controller;

import com.grupo10.medication_service.dto.IntakeHistoryResponseDto;
import com.grupo10.medication_service.exception.BusinessLogicException;
import com.grupo10.medication_service.exception.GlobalExceptionHandler;
import com.grupo10.medication_service.exception.ResourceNotFoundException;
import com.grupo10.medication_service.service.IntakeHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IntakeHistoryControllerTest {

    @Mock
    private IntakeHistoryService intakeHistoryService;

    @InjectMocks
    private IntakeHistoryController intakeHistoryController;

    private MockMvc mockMvc;
    private IntakeHistoryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(intakeHistoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        responseDto = new IntakeHistoryResponseDto();
        responseDto.setId(1L);
        responseDto.setIdMedicamento(1L);
        responseDto.setFechaHora(LocalDateTime.of(2026, 1, 10, 8, 0));
        responseDto.setFechaHoraReal(null);
        responseDto.setTomado(false);
    }

    // --- GET /api/intake-history/{id} ---

    @Test
    void getIntakeHistoryById_existente_retorna200() throws Exception {
        when(intakeHistoryService.getIntakeHistoryById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/intake-history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tomado").value(false));
    }

    @Test
    void getIntakeHistoryById_noExistente_retorna404() throws Exception {
        when(intakeHistoryService.getIntakeHistoryById(99L))
                .thenThrow(new ResourceNotFoundException("Historial de Toma", "id", 99L));

        mockMvc.perform(get("/api/intake-history/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- GET /api/intake-history/medication/{idMedicamento} ---

    @Test
    void getIntakeHistoriesByMedicationId_retorna200() throws Exception {
        when(intakeHistoryService.getIntakeHistoriesByMedicationId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/intake-history/medication/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMedicamento").value(1));
    }

    @Test
    void getIntakeHistoriesByMedicationId_sinHistorial_retornaListaVacia() throws Exception {
        when(intakeHistoryService.getIntakeHistoriesByMedicationId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/intake-history/medication/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /api/intake-history/medication/{idMedicamento}/pending ---

    @Test
    void getPendingIntakesByMedicationId_retorna200() throws Exception {
        when(intakeHistoryService.getPendingIntakesByMedicationId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/intake-history/medication/1/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tomado").value(false));
    }

    @Test
    void getPendingIntakesByMedicationId_sinPendientes_retornaListaVacia() throws Exception {
        when(intakeHistoryService.getPendingIntakesByMedicationId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/intake-history/medication/1/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- PATCH /api/intake-history/{id}/mark-taken ---

    @Test
    void markAsTaken_pendiente_retorna200() throws Exception {
        IntakeHistoryResponseDto taken = new IntakeHistoryResponseDto();
        taken.setId(1L);
        taken.setIdMedicamento(1L);
        taken.setFechaHora(LocalDateTime.of(2026, 1, 10, 8, 0));
        taken.setFechaHoraReal(LocalDateTime.of(2026, 1, 10, 8, 5));
        taken.setTomado(true);

        when(intakeHistoryService.markAsTaken(1L)).thenReturn(taken);

        mockMvc.perform(patch("/api/intake-history/1/mark-taken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tomado").value(true))
                .andExpect(jsonPath("$.fechaHoraReal").isNotEmpty());
    }

    @Test
    void markAsTaken_yaFueTomado_retorna409() throws Exception {
        when(intakeHistoryService.markAsTaken(1L))
                .thenThrow(new BusinessLogicException("El medicamento con ID 1 ya fue marcado como tomado"));

        mockMvc.perform(patch("/api/intake-history/1/mark-taken"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void markAsTaken_noExistente_retorna404() throws Exception {
        when(intakeHistoryService.markAsTaken(99L))
                .thenThrow(new ResourceNotFoundException("Historial de toma", "id", 99L));

        mockMvc.perform(patch("/api/intake-history/99/mark-taken"))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /api/intake-history/{id} ---

    @Test
    void deleteIntakeHistory_existente_retorna204() throws Exception {
        doNothing().when(intakeHistoryService).deleteIntakeHistory(1L);

        mockMvc.perform(delete("/api/intake-history/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteIntakeHistory_noExistente_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("Historial de toma", "id", 99L))
                .when(intakeHistoryService).deleteIntakeHistory(99L);

        mockMvc.perform(delete("/api/intake-history/99"))
                .andExpect(status().isNotFound());
    }
}