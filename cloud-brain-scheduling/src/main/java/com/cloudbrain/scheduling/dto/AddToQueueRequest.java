package com.cloudbrain.scheduling.dto;

public record AddToQueueRequest(
        String doctorId,
        String patientId,
        String patientName,
        String source) {}
