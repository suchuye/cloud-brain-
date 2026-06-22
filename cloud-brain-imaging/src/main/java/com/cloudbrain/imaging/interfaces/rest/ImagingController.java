package com.cloudbrain.imaging.interfaces.rest;

import com.cloudbrain.imaging.application.ImagingService;
import com.cloudbrain.imaging.domain.entity.AiReport;
import com.cloudbrain.imaging.domain.entity.ImagingTask;
import com.cloudbrain.imaging.dto.SubmitImageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing endpoints for imaging task submission and result retrieval.
 * <p>Provides image submission, task status lookup, and AI report retrieval.</p>
 */
@RestController
@RequestMapping("/api/imaging")
public class ImagingController {

    private final ImagingService imagingService;

    public ImagingController(ImagingService imagingService) {
        this.imagingService = imagingService;
    }

    /** Submits a new image for AI analysis and triggers the processing pipeline. */
    @PostMapping("/submit")
    public ResponseEntity<ImagingTask> submitImage(@RequestBody SubmitImageRequest req) {
        return ResponseEntity.ok(imagingService.submitImage(req));
    }

    /** Retrieves the current state and metadata of an imaging task. */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<ImagingTask> getTask(@PathVariable String id) {
        return ResponseEntity.ok(imagingService.getTask(id));
    }

    /** Retrieves the AI-generated report for a completed imaging task. */
    @GetMapping("/reports/{taskId}")
    public ResponseEntity<AiReport> getReport(@PathVariable String taskId) {
        return ResponseEntity.ok(imagingService.getReport(taskId));
    }
}
