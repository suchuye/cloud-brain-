package com.cloudbrain.outpatient.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record PatientInfo(
        String patientId,
        String patientName,
        String gender,
        int age) {
    public PatientInfo() { this(null, null, null, 0); }
}
