package com.cloudbrain.scheduling.domain.entity;

import com.cloudbrain.scheduling.domain.vo.QueueStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate root for the queue bounded context.
 * Manages patient queue state machine: WAITING -> IN_QUEUE -> IN_CONSULTATION -> {COMPLETED, PASSED},
 * with CANCELLED as a terminal state reachable from any non-completed status.
 */
@Entity
@Table(name = "queue")
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String doctorId;
    private String patientId;
    private String patientName;

    private int queueNumber;  // 候诊序号

    @Enumerated(EnumType.STRING)
    private QueueStatus status = QueueStatus.WAITING;

    private String source;    // ONLINE / OFFLINE

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @Transient
    private final List<Object> events = new ArrayList<>();

    public Queue() {}

    public Queue(String doctorId, String patientId, String patientName, int queueNumber, String source) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.queueNumber = queueNumber;
        this.source = source;
    }

    /**
     * Transitions from WAITING to IN_QUEUE (patient has arrived and checked in).
     */
    public void checkIn() {
        if (status != QueueStatus.WAITING) throw new IllegalStateException("Not waiting");
        status = QueueStatus.IN_QUEUE;
        updatedAt = Instant.now();
    }

    /**
     * Transitions from IN_QUEUE to IN_CONSULTATION (doctor called the patient).
     */
    public void call() {
        if (status != QueueStatus.IN_QUEUE) throw new IllegalStateException("Not in queue");
        status = QueueStatus.IN_CONSULTATION;
        updatedAt = Instant.now();
        events.add("CALLED");
    }

    /**
     * Transitions from IN_CONSULTATION to COMPLETED (consultation finished normally).
     */
    public void complete() {
        if (status != QueueStatus.IN_CONSULTATION) throw new IllegalStateException("Not in consultation");
        status = QueueStatus.COMPLETED;
        updatedAt = Instant.now();
        events.add("COMPLETED");
    }

    /**
     * Transitions from IN_CONSULTATION to PASSED (patient did not show).
     */
    public void pass() {
        if (status != QueueStatus.IN_CONSULTATION) throw new IllegalStateException("Not in consultation");
        status = QueueStatus.PASSED;
        updatedAt = Instant.now();
        events.add("PASSED");
    }

    /**
     * Transitions to CANCELLED from any status except COMPLETED.
     */
    public void cancel() {
        if (status == QueueStatus.COMPLETED) throw new IllegalStateException("Already completed");
        status = QueueStatus.CANCELLED;
        updatedAt = Instant.now();
        events.add("CANCELLED");
    }

    public List<Object> getEvents() { return List.copyOf(events); }
    public void clearEvents() { events.clear(); }

    public String getId() { return id; }
    public String getDoctorId() { return doctorId; }
    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getQueueNumber() { return queueNumber; }
    public QueueStatus getStatus() { return status; }
    public String getSource() { return source; }
    public Instant getUpdatedAt() { return updatedAt; }
}
