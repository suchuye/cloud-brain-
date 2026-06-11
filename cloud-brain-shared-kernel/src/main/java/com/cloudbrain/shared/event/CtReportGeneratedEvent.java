package com.cloudbrain.shared.event;

public class CtReportGeneratedEvent extends DomainEvent {
    private String imagingTaskId;
    private String consultationId;
    private String reportId;
    private boolean criticalWarning;

    public CtReportGeneratedEvent() {}
    public CtReportGeneratedEvent(String imagingTaskId, String consultationId, String reportId, boolean criticalWarning) {
        this.imagingTaskId = imagingTaskId;
        this.consultationId = consultationId;
        this.reportId = reportId;
        this.criticalWarning = criticalWarning;
    }

    public String getImagingTaskId() { return imagingTaskId; }
    public String getConsultationId() { return consultationId; }
    public String getReportId() { return reportId; }
    public boolean isCriticalWarning() { return criticalWarning; }
}
