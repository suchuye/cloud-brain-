package com.cloudbrain.shared.event;

public class ConsultationStartedEvent extends DomainEvent {
    private String consultationId;
    private String doctorId;
    private String patientId;

    public ConsultationStartedEvent() {}
    public ConsultationStartedEvent(String consultationId, String doctorId, String patientId) {
        this.consultationId = consultationId;
        this.doctorId = doctorId;
        this.patientId = patientId;
    }

    public String getConsultationId() { return consultationId; }
    public String getDoctorId() { return doctorId; }
    public String getPatientId() { return patientId; }
}
