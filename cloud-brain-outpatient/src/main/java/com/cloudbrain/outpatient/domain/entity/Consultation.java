package com.cloudbrain.outpatient.domain.entity;

import com.cloudbrain.outpatient.domain.event.ConsultationFinishedDomainEvent;
import com.cloudbrain.outpatient.domain.event.ConsultationStartedDomainEvent;
import com.cloudbrain.outpatient.domain.vo.PatientInfo;
import com.cloudbrain.shared.enums.ConsultationStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String doctorId;
    private String doctorName;

    @Embedded
    private PatientInfo patientInfo;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.WAITING;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    public Consultation() {}

    public Consultation(String doctorId, String doctorName, PatientInfo patientInfo) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientInfo = patientInfo;
    }

    public void start() {
        if (this.status != ConsultationStatus.WAITING) {
            throw new IllegalStateException("Consultation must be waiting to start");
        }
        this.status = ConsultationStatus.IN_PROGRESS;
        this.updatedAt = Instant.now();
        domainEvents.add(new ConsultationStartedDomainEvent(this.id, this.doctorId, this.patientInfo.getPatientId()));
    }

    public void pass() {
        this.status = ConsultationStatus.PASSED;
        this.updatedAt = Instant.now();
    }

    public void finish() {
        if (this.status != ConsultationStatus.IN_PROGRESS) {
            throw new IllegalStateException("Consultation must be in progress to finish");
        }
        this.status = ConsultationStatus.FINISHED;
        this.updatedAt = Instant.now();
        domainEvents.add(new ConsultationFinishedDomainEvent(this.id));
    }

    public List<Object> getDomainEvents() { return List.copyOf(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public String getId() { return id; }
    public String getDoctorId() { return doctorId; }
    public PatientInfo getPatientInfo() { return patientInfo; }
    public ConsultationStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
