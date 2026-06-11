package com.cloudbrain.patient.interfaces.rest;

import com.cloudbrain.patient.application.PatientService;
import com.cloudbrain.patient.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/triage")
    public ResponseEntity<Map<String, String>> triage(@RequestBody TriageRequest req) {
        return ResponseEntity.ok(patientService.triage(req));
    }

    @PostMapping("/book")
    public ResponseEntity<Map<String, String>> book(@RequestBody BookRequest req) {
        return ResponseEntity.ok(patientService.book(req));
    }

    @GetMapping("/reports/{patientId}")
    public ResponseEntity<?> getReports(@PathVariable String patientId) {
        return ResponseEntity.ok(patientService.getReports(patientId));
    }
}
