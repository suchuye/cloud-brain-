package com.cloudbrain.outpatient.domain.event;

/**
 * Domain event published when a consultation transitions from WAITING to IN_PROGRESS.
 */
public record ConsultationStartedDomainEvent(
        String consultationId,
        String doctorId,
        String patientId) {}
