package com.cloudbrain.shared.event;

/**
 * 接诊开始事件。
 * 门诊工作台医生叫号后发布，AI 助理消费后初始化诊断会话。
 */
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
