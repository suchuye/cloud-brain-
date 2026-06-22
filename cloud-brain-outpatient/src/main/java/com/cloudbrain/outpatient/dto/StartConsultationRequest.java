package com.cloudbrain.outpatient.dto;

/**
 * Request DTO carrying consultationId and doctorId to start a consultation.
 */
public record StartConsultationRequest(
        String consultationId,
        String doctorId) {}
