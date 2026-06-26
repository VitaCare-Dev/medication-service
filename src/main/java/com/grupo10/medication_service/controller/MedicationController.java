package com.grupo10.medication_service.controller;

import com.grupo10.medication_service.dto.MedicationRequestDto;
import com.grupo10.medication_service.dto.MedicationResponseDto;
import com.grupo10.medication_service.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<MedicationResponseDto> createMedication(@RequestBody MedicationRequestDto requestDto) {
        // Nota: Asegúrate de que este método en el service sea el que genera el
        // historial automáticamente
        MedicationResponseDto responseDto = medicationService.createTreatment(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> getMedicationById(@PathVariable Long id) {
        MedicationResponseDto medication = medicationService.getMedicationById(id);
        return new ResponseEntity<>(medication, HttpStatus.OK);
    }

    @GetMapping("/patient/{idPaciente}")
    public ResponseEntity<List<MedicationResponseDto>> getMedicationsByPatientId(@PathVariable Long idPaciente) {
        List<MedicationResponseDto> medications = medicationService.getMedicationsByPatientId(idPaciente);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @GetMapping("/patient/{idPaciente}/active")
    public ResponseEntity<List<MedicationResponseDto>> getActiveMedicationsByPatientId(@PathVariable Long idPaciente) {
        List<MedicationResponseDto> medications = medicationService.getActiveMedicationsByPatientId(idPaciente);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<MedicationResponseDto> deactivateMedication(@PathVariable Long id) {
        MedicationResponseDto deactivatedMedication = medicationService.deactivateMedication(id);
        return new ResponseEntity<>(deactivatedMedication, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}