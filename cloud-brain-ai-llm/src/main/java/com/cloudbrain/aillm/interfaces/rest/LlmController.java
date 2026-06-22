package com.cloudbrain.aillm.interfaces.rest;

import com.cloudbrain.aillm.application.LlmInferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller exposing LLM inference endpoints.
 * Provides diagnosis and summarization capabilities to front-end clients.
 */
@RestController
@RequestMapping("/api/llm")
public class LlmController {

    private final LlmInferenceService llmService;

    public LlmController(LlmInferenceService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/diagnose")
    public ResponseEntity<Map<String, String>> diagnose(@RequestBody Map<String, String> req) {
        String dialogHistory = req.getOrDefault("dialog", "");
        String result = llmService.diagnose(dialogHistory);
        return ResponseEntity.ok(Map.of("result", result));
    }

    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarize(@RequestBody Map<String, String> req) {
        String dialogHistory = req.getOrDefault("dialog", "");
        String result = llmService.summarize(dialogHistory);
        return ResponseEntity.ok(Map.of("result", result));
    }
}
