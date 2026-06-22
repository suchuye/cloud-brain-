package com.cloudbrain.patient.dto;

/**
 * Request record containing patient and department identifiers for appointment booking.
 */
public record BookRequest(String patientId, String departmentId) {}
