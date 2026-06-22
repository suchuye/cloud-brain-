package com.cloudbrain.scheduling.dto;

/**
 * Request DTO for adding a patient to the doctor's queue.
 * The source field indicates the registration channel (ONLINE or OFFLINE).
 */
public record AddToQueueRequest(
        String doctorId,
        String patientId,
        String patientName,
        String source) {}
