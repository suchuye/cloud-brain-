package com.cloudbrain.imaging.interfaces.rest;

import com.cloudbrain.imaging.application.ImagingService;
import com.cloudbrain.imaging.domain.entity.AiReport;
import com.cloudbrain.imaging.domain.entity.ImagingTask;
import com.cloudbrain.imaging.dto.SubmitImageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imaging")
public class ImagingController {

    private final ImagingService imagingService;

    public ImagingController(ImagingService imagingService) {
        this.imagingService = imagingService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ImagingTask> submitImage(@RequestBody SubmitImageRequest req) {
        return ResponseEntity.ok(imagingService.submitImage(req));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ImagingTask> getTask(@PathVariable String id) {
        return ResponseEntity.ok(imagingService.getTask(id));
    }

    @GetMapping("/reports/{taskId}")
    public ResponseEntity<AiReport> getReport(@PathVariable String taskId) {
        return ResponseEntity.ok(imagingService.getReport(taskId));
    }
}
