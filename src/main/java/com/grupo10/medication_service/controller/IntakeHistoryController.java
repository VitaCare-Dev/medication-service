package com.grupo10.medication_service.controller;

import com.grupo10.medication_service.dto.IntakeHistoryResponseDto;
import com.grupo10.medication_service.service.IntakeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intake-history")
@RequiredArgsConstructor
public class IntakeHistoryController {

    private final IntakeHistoryService intakeHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<IntakeHistoryResponseDto> getIntakeHistoryById(@PathVariable Long id) {
        IntakeHistoryResponseDto intakeHistory = intakeHistoryService.getIntakeHistoryById(id);
        return new ResponseEntity<>(intakeHistory, HttpStatus.OK);
    }

    @GetMapping("/medication/{idMedicamento}")
    public ResponseEntity<List<IntakeHistoryResponseDto>> getIntakeHistoriesByMedicationId(
            @PathVariable Long idMedicamento) {
        List<IntakeHistoryResponseDto> intakeHistories = intakeHistoryService
                .getIntakeHistoriesByMedicationId(idMedicamento);
        return new ResponseEntity<>(intakeHistories, HttpStatus.OK);
    }

    @GetMapping("/medication/{idMedicamento}/pending")
    public ResponseEntity<List<IntakeHistoryResponseDto>> getPendingIntakesByMedicationId(
            @PathVariable Long idMedicamento) {
        List<IntakeHistoryResponseDto> pendingIntakes = intakeHistoryService
                .getPendingIntakesByMedicationId(idMedicamento);
        return new ResponseEntity<>(pendingIntakes, HttpStatus.OK);
    }

    @PatchMapping("/{id}/mark-taken")
    public ResponseEntity<IntakeHistoryResponseDto> markAsTaken(@PathVariable Long id) {
        IntakeHistoryResponseDto markedIntake = intakeHistoryService.markAsTaken(id);
        return new ResponseEntity<>(markedIntake, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntakeHistory(@PathVariable Long id) {
        intakeHistoryService.deleteIntakeHistory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}