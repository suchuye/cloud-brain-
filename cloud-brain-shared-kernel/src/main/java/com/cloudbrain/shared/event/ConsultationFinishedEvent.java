package com.cloudbrain.shared.event;

/**
 * 就诊结束事件。
 * 医生点击结束就诊后发布，触发病历归档和 AI 摘要生成。
 */
public class ConsultationFinishedEvent extends DomainEvent {
    private String consultationId;

    public ConsultationFinishedEvent() {}
    public ConsultationFinishedEvent(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getConsultationId() { return consultationId; }
}
