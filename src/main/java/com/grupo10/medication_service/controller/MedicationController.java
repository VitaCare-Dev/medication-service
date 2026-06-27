package com.grupo10.medication_service.controller;

import com.grupo10.medication_service.dto.MedicationRequestDto;
import com.grupo10.medication_service.dto.MedicationResponseDto;
import com.grupo10.medication_service.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints de gestión de tratamientos farmacológicos.
 *
 * <p>Base URL: {@code /api/medications}. Todas las respuestas de error son manejadas
 * centralmente por {@link com.grupo10.medication_service.exception.GlobalExceptionHandler}.
 */
@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    /**
     * Registra un nuevo tratamiento farmacológico.
     *
     * <p>{@code POST /api/medications}
     *
     * @param requestDto datos del tratamiento a crear
     * @return el tratamiento creado con su ID asignado y estado HTTP {@code 201 Created}
     */
    @PostMapping
    public ResponseEntity<MedicationResponseDto> createMedication(@RequestBody MedicationRequestDto requestDto) {
        MedicationResponseDto responseDto = medicationService.createTreatment(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Obtiene un tratamiento por su identificador único.
     *
     * <p>{@code GET /api/medications/{id}}
     *
     * @param id identificador del tratamiento
     * @return el tratamiento encontrado y estado HTTP {@code 200 OK}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> getMedicationById(@PathVariable Long id) {
        MedicationResponseDto medication = medicationService.getMedicationById(id);
        return new ResponseEntity<>(medication, HttpStatus.OK);
    }

    /**
     * Lista todos los tratamientos de un paciente, activos e inactivos.
     *
     * <p>{@code GET /api/medications/patient/{idPaciente}}
     *
     * @param idPaciente identificador del paciente
     * @return lista de tratamientos del paciente y estado HTTP {@code 200 OK}
     */
    @GetMapping("/patient/{idPaciente}")
    public ResponseEntity<List<MedicationResponseDto>> getMedicationsByPatientId(@PathVariable Long idPaciente) {
        List<MedicationResponseDto> medications = medicationService.getMedicationsByPatientId(idPaciente);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    /**
     * Lista únicamente los tratamientos activos de un paciente.
     *
     * <p>{@code GET /api/medications/patient/{idPaciente}/active}
     *
     * @param idPaciente identificador del paciente
     * @return lista de tratamientos con estado activo y estado HTTP {@code 200 OK}
     */
    @GetMapping("/patient/{idPaciente}/active")
    public ResponseEntity<List<MedicationResponseDto>> getActiveMedicationsByPatientId(@PathVariable Long idPaciente) {
        List<MedicationResponseDto> medications = medicationService.getActiveMedicationsByPatientId(idPaciente);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    /**
     * Desactiva un tratamiento sin eliminarlo de la base de datos.
     *
     * <p>{@code PATCH /api/medications/{id}/deactivate}
     *
     * @param id identificador del tratamiento a desactivar
     * @return el tratamiento actualizado con estado {@code activo = 0} y HTTP {@code 200 OK}
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<MedicationResponseDto> deactivateMedication(@PathVariable Long id) {
        MedicationResponseDto deactivatedMedication = medicationService.deactivateMedication(id);
        return new ResponseEntity<>(deactivatedMedication, HttpStatus.OK);
    }

    /**
     * Elimina permanentemente un tratamiento de la base de datos.
     *
     * <p>{@code DELETE /api/medications/{id}}
     *
     * @param id identificador del tratamiento a eliminar
     * @return respuesta vacía con estado HTTP {@code 204 No Content}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
