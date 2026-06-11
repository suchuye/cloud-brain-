package com.cloudbrain.shared.event;

public class ConsultationAiSummaryEvent extends DomainEvent {
    private String consultationId;
    private String summaryJson;

    public ConsultationAiSummaryEvent() {}
    public ConsultationAiSummaryEvent(String consultationId, String summaryJson) {
        this.consultationId = consultationId;
        this.summaryJson = summaryJson;
    }

    public String getConsultationId() { return consultationId; }
    public String getSummaryJson() { return summaryJson; }
}
