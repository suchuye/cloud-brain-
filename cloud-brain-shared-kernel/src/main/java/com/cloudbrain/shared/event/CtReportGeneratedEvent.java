package com.cloudbrain.shared.event;

/**
 * CT 报告生成事件。
 * 医学影像 AI 处理完成后发布，门诊工作台消费后
 * 在医生端弹窗/红点提示查阅报告。
 */
public class CtReportGeneratedEvent extends DomainEvent {
    private String imagingTaskId;
    private String consultationId;
    private String reportId;
    /** 是否包含危急值预警 */
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
