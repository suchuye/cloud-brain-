package com.cloudbrain.shared.event;

public class QueueUpdatedEvent extends DomainEvent {
    private String queueId;
    private String doctorId;
    private String changeType; // SIGN_IN, CANCEL, CALL, PASS

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
