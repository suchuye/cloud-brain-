package com.cloudbrain.shared.event;

public class ConsultationFinishedEvent extends DomainEvent {
    private String consultationId;

    public ConsultationFinishedEvent() {}
    public ConsultationFinishedEvent(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getConsultationId() { return consultationId; }
}
