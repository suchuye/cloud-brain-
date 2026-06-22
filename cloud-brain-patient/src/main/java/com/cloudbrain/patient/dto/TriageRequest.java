package com.cloudbrain.patient.dto;

/**
 * Request record carrying the patient's symptom description for triage.
 */
public record TriageRequest(String symptom) {}
