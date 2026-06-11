package com.cloudbrain.outpatient.domain.event;

public record ConsultationStartedDomainEvent(
        String consultationId,
        String doctorId,
        String patientId) {}
