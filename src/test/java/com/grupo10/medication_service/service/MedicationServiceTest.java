package com.grupo10.medication_service.service;

import com.grupo10.medication_service.dto.MedicationRequestDto;
import com.grupo10.medication_service.dto.MedicationResponseDto;
import com.grupo10.medication_service.exception.BusinessLogicException;
import com.grupo10.medication_service.exception.ResourceNotFoundException;
import com.grupo10.medication_service.exception.ValidationException;
import com.grupo10.medication_service.model.Medication;
import com.grupo10.medication_service.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    private Medication medication;
    private MedicationRequestDto requestDto;

    @BeforeEach
    void setUp() {
        medication = new Medication();
        medication.setIdMedicamento(1L);
        medication.setIdPaciente(10L);
        medication.setNombreMedicamento("Paracetamol");
        medication.setDosis("500mg");
        medication.setFrecuenciaHoras(8);
        medication.setFechaInicio(LocalDate.of(2026, 1, 1));
        medication.setFechaTermino(LocalDate.of(2026, 1, 31));
        medication.setActivo(1);

        requestDto = new MedicationRequestDto();
        requestDto.setIdPaciente(10L);
        requestDto.setNombreMedicamento("Paracetamol");
        requestDto.setDosis("500mg");
        requestDto.setFrecuenciaHoras(8);
        requestDto.setFechaInicio(LocalDate.of(2026, 1, 1));
        requestDto.setFechaTermino(LocalDate.of(2026, 1, 31));
        requestDto.setActivo(1);
    }

    // --- createTreatment ---

    @Test
    void createTreatment_conDatosValidos_retornaMedicamento() {
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        MedicationResponseDto result = medicationService.createTreatment(requestDto);

        assertThat(result.getIdMedicamento()).isEqualTo(1L);
        assertThat(result.getNombreMedicamento()).isEqualTo("Paracetamol");
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void createTreatment_sinIdPaciente_lanzaValidationException() {
        requestDto.setIdPaciente(null);

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("idPaciente");
    }

    @Test
    void createTreatment_nombreMedicamentoVacio_lanzaValidationException() {
        requestDto.setNombreMedicamento("  ");

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("nombreMedicamento");
    }

    @Test
    void createTreatment_nombreMedicamentoNulo_lanzaValidationException() {
        requestDto.setNombreMedicamento(null);

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("nombreMedicamento");
    }

    @Test
    void createTreatment_dosisVacia_lanzaValidationException() {
        requestDto.setDosis("");

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("dosis");
    }

    @Test
    void createTreatment_frecuenciaHorasCero_lanzaValidationException() {
        requestDto.setFrecuenciaHoras(0);

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("frecuenciaHoras");
    }

    @Test
    void createTreatment_frecuenciaHorasNegativa_lanzaValidationException() {
        requestDto.setFrecuenciaHoras(-1);

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("frecuenciaHoras");
    }

    @Test
    void createTreatment_sinFechaInicio_lanzaValidationException() {
        requestDto.setFechaInicio(null);

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("fechaInicio");
    }

    @Test
    void createTreatment_fechaTerminoAnteriorAInicio_lanzaBusinessLogicException() {
        requestDto.setFechaTermino(LocalDate.of(2025, 12, 31));

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("fecha de término");
    }

    @Test
    void createTreatment_dosisNula_lanzaValidationException() {
        requestDto.setDosis(null);

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("dosis");
    }

    @Test
    void createTreatment_dosisEnBlanco_lanzaValidationException() {
        requestDto.setDosis("   ");

        assertThatThrownBy(() -> medicationService.createTreatment(requestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("dosis");
    }

    @Test
    void createTreatment_sinFechaTermino_esValido() {
        requestDto.setFechaTermino(null);
        medication.setFechaTermino(null);
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        MedicationResponseDto result = medicationService.createTreatment(requestDto);

        assertThat(result.getFechaTermino()).isNull();
    }

    // --- getMedicationById ---

    @Test
    void getMedicationById_existente_retornaMedicamento() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        MedicationResponseDto result = medicationService.getMedicationById(1L);

        assertThat(result.getIdMedicamento()).isEqualTo(1L);
    }

    @Test
    void getMedicationById_noExistente_lanzaResourceNotFoundException() {
        when(medicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicationService.getMedicationById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- getMedicationsByPatientId ---

    @Test
    void getMedicationsByPatientId_retornaLista() {
        when(medicationRepository.findByIdPaciente(10L)).thenReturn(List.of(medication));

        List<MedicationResponseDto> result = medicationService.getMedicationsByPatientId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdPaciente()).isEqualTo(10L);
    }

    @Test
    void getMedicationsByPatientId_sinMedicamentos_retornaListaVacia() {
        when(medicationRepository.findByIdPaciente(99L)).thenReturn(List.of());

        List<MedicationResponseDto> result = medicationService.getMedicationsByPatientId(99L);

        assertThat(result).isEmpty();
    }

    // --- getActiveMedicationsByPatientId ---

    @Test
    void getActiveMedicationsByPatientId_retornaActivos() {
        when(medicationRepository.findByIdPacienteAndActivo(10L, 1)).thenReturn(List.of(medication));

        List<MedicationResponseDto> result = medicationService.getActiveMedicationsByPatientId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActivo()).isEqualTo(1);
    }

    // --- deleteMedication ---

    @Test
    void deleteMedication_existente_eliminaCorrectamente() {
        when(medicationRepository.existsById(1L)).thenReturn(true);

        medicationService.deleteMedication(1L);

        verify(medicationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedication_noExistente_lanzaResourceNotFoundException() {
        when(medicationRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> medicationService.deleteMedication(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- deactivateMedication ---

    @Test
    void deactivateMedication_activo_desactivaCorrectamente() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        medication.setActivo(1);
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);

        MedicationResponseDto result = medicationService.deactivateMedication(1L);

        assertThat(result.getActivo()).isEqualTo(0);
    }

    @Test
    void deactivateMedication_yaInactivo_lanzaBusinessLogicException() {
        medication.setActivo(0);
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        assertThatThrownBy(() -> medicationService.deactivateMedication(1L))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessageContaining("ya está inactivo");
    }

    @Test
    void deactivateMedication_noExistente_lanzaResourceNotFoundException() {
        when(medicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicationService.deactivateMedication(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}