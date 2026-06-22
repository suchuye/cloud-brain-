package com.cloudbrain.shared.event;

/**
 * AI 助理会话摘要事件。
 * 就诊结束后 AI 助理生成结构化摘要（主诉、鉴别诊断、用药建议），
 * 门诊工作台消费后写入病历归档。
 */
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
