package com.cloudbrain.outpatient.interfaces.rest;

import com.cloudbrain.outpatient.application.ConsultationService;
import com.cloudbrain.outpatient.dto.StartConsultationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing consultation lifecycle endpoints (start, finish).
 * All endpoints require the DOCTOR role.
 */
@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping("/start")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> startConsultation(@RequestBody StartConsultationRequest request) {
        consultationService.startConsultation(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finish")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> finishConsultation(@PathVariable String id) {
        consultationService.finishConsultation(id);
        return ResponseEntity.ok().build();
    }
}
