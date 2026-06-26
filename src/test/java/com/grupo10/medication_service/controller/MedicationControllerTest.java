package com.grupo10.medication_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grupo10.medication_service.dto.MedicationRequestDto;
import com.grupo10.medication_service.dto.MedicationResponseDto;
import com.grupo10.medication_service.exception.BusinessLogicException;
import com.grupo10.medication_service.exception.GlobalExceptionHandler;
import com.grupo10.medication_service.exception.ResourceNotFoundException;
import com.grupo10.medication_service.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MedicationControllerTest {

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private MedicationController medicationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MedicationResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        responseDto = new MedicationResponseDto();
        responseDto.setIdMedicamento(1L);
        responseDto.setIdPaciente(10L);
        responseDto.setNombreMedicamento("Paracetamol");
        responseDto.setDosis("500mg");
        responseDto.setFrecuenciaHoras(8);
        responseDto.setFechaInicio(LocalDate.of(2026, 1, 1));
        responseDto.setFechaTermino(LocalDate.of(2026, 1, 31));
        responseDto.setActivo(1);
    }

    // --- POST /api/medications ---

    @Test
    void createMedication_conDatosValidos_retorna201() throws Exception {
        MedicationRequestDto requestDto = buildRequestDto();
        when(medicationService.createTreatment(any(MedicationRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMedicamento").value(1))
                .andExpect(jsonPath("$.nombreMedicamento").value("Paracetamol"));
    }

    @Test
    void createMedication_serviceLanzaValidationException_retorna400() throws Exception {
        MedicationRequestDto requestDto = buildRequestDto();
        when(medicationService.createTreatment(any(MedicationRequestDto.class)))
                .thenThrow(new com.grupo10.medication_service.exception.ValidationException("idPaciente", "no puede ser nulo"));

        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createMedication_serviceLanzaBusinessLogicException_retorna409() throws Exception {
        MedicationRequestDto requestDto = buildRequestDto();
        when(medicationService.createTreatment(any(MedicationRequestDto.class)))
                .thenThrow(new BusinessLogicException("La fecha de término no puede ser anterior a la fecha de inicio"));

        mockMvc.perform(post("/api/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    // --- GET /api/medications/{id} ---

    @Test
    void getMedicationById_existente_retorna200() throws Exception {
        when(medicationService.getMedicationById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/medications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedicamento").value(1))
                .andExpect(jsonPath("$.dosis").value("500mg"));
    }

    @Test
    void getMedicationById_noExistente_retorna404() throws Exception {
        when(medicationService.getMedicationById(99L))
                .thenThrow(new ResourceNotFoundException("Medicamento", "id", 99L));

        mockMvc.perform(get("/api/medications/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- GET /api/medications/patient/{idPaciente} ---

    @Test
    void getMedicationsByPatientId_retorna200() throws Exception {
        when(medicationService.getMedicationsByPatientId(10L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/medications/patient/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPaciente").value(10));
    }

    @Test
    void getMedicationsByPatientId_sinMedicamentos_retornaListaVacia() throws Exception {
        when(medicationService.getMedicationsByPatientId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/medications/patient/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /api/medications/patient/{idPaciente}/active ---

    @Test
    void getActiveMedicationsByPatientId_retorna200() throws Exception {
        when(medicationService.getActiveMedicationsByPatientId(10L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/medications/patient/10/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(1));
    }

    // --- PATCH /api/medications/{id}/deactivate ---

    @Test
    void deactivateMedication_activo_retorna200() throws Exception {
        responseDto.setActivo(0);
        when(medicationService.deactivateMedication(1L)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/medications/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(0));
    }

    @Test
    void deactivateMedication_yaInactivo_retorna409() throws Exception {
        when(medicationService.deactivateMedication(1L))
                .thenThrow(new BusinessLogicException("El medicamento con ID 1 ya está inactivo"));

        mockMvc.perform(patch("/api/medications/1/deactivate"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deactivateMedication_noExistente_retorna404() throws Exception {
        when(medicationService.deactivateMedication(99L))
                .thenThrow(new ResourceNotFoundException("Medicamento", "id", 99L));

        mockMvc.perform(patch("/api/medications/99/deactivate"))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /api/medications/{id} ---

    @Test
    void deleteMedication_existente_retorna204() throws Exception {
        doNothing().when(medicationService).deleteMedication(1L);

        mockMvc.perform(delete("/api/medications/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMedication_noExistente_retorna404() throws Exception {
        doThrow(new ResourceNotFoundException("Medicamento", "id", 99L))
                .when(medicationService).deleteMedication(99L);

        mockMvc.perform(delete("/api/medications/99"))
                .andExpect(status().isNotFound());
    }

    private MedicationRequestDto buildRequestDto() {
        MedicationRequestDto dto = new MedicationRequestDto();
        dto.setIdPaciente(10L);
        dto.setNombreMedicamento("Paracetamol");
        dto.setDosis("500mg");
        dto.setFrecuenciaHoras(8);
        dto.setFechaInicio(LocalDate.of(2026, 1, 1));
        dto.setFechaTermino(LocalDate.of(2026, 1, 31));
        dto.setActivo(1);
        return dto;
    }
}