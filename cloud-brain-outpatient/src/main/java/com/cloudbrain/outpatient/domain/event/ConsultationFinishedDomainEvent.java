package com.cloudbrain.outpatient.domain.event;

/**
 * Domain event published when a consultation transitions to FINISHED status.
 */
public record ConsultationFinishedDomainEvent(String consultationId) {}
