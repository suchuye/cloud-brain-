package com.cloudbrain.outpatient.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "emr")
public class Emr {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String consultationId;

    @Column(columnDefinition = "JSON")
    private String content; // 动态病历模板的 JSON

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Emr() {}

    public Emr(String consultationId, String content) {
        this.consultationId = consultationId;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getConsultationId() { return consultationId; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
