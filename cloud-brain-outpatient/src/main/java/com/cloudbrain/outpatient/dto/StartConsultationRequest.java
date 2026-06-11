package com.cloudbrain.outpatient.dto;

public record StartConsultationRequest(
        String consultationId,
        String doctorId) {}
