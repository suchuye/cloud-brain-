package com.cloudbrain.outpatient.interfaces.rest;

import com.cloudbrain.outpatient.application.EmrService;
import com.cloudbrain.outpatient.domain.entity.Emr;
import com.cloudbrain.outpatient.infrastructure.search.EmrDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emr")
public class EmrController {

    private final EmrService emrService;

    public EmrController(EmrService emrService) {
        this.emrService = emrService;
    }

    @PostMapping
    public ResponseEntity<Emr> save(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(emrService.saveEmr(
                req.get("consultationId"), req.get("content")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emr> update(@PathVariable String id, @RequestBody Map<String, String> req) {
        return ResponseEntity.ok(emrService.updateEmr(id, req.get("content")));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmrDocument>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(emrService.search(keyword));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<EmrDocument>> patientHistory(@PathVariable String patientId) {
        return ResponseEntity.ok(emrService.searchByPatient(patientId));
    }
}
