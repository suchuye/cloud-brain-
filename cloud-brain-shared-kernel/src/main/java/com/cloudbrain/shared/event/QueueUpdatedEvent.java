package com.cloudbrain.shared.event;

/**
 * 候诊队列变更事件。
 * 签到、叫号、过号、取消等操作触发，
 * 门诊工作台消费后实时刷新候诊列表。
 */
public class QueueUpdatedEvent extends DomainEvent {
    private String queueId;
    private String doctorId;
    /** 变更类型：CHECK_IN / CALL / COMPLETE / PASS / CANCEL */
    private String changeType;

    public QueueUpdatedEvent() {}
    public QueueUpdatedEvent(String queueId, String doctorId, String changeType) {
        this.queueId = queueId;
        this.doctorId = doctorId;
        this.changeType = changeType;
    }

    public String getQueueId() { return queueId; }
    public String getDoctorId() { return doctorId; }
    public String getChangeType() { return changeType; }
}
