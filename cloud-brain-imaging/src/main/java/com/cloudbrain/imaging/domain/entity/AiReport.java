package com.cloudbrain.imaging.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Domain entity representing an AI-generated radiology report.
 * <p>Carries structured findings (JSON), quantitative measurements (JSON),
 * a textual impression, and a critical-warning flag for urgent results.</p>
 */
@Entity
@Table(name = "ai_report")
public class AiReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String imagingTaskId;
    private String consultationId;

    @Column(columnDefinition = "JSON")
    private String findings;       // 病灶特征 JSON

    @Column(columnDefinition = "JSON")
    private String measurements;   // 定量参数 JSON

    private String impression;     // 初步诊断建议
    private boolean criticalWarning;  // 危机值预警

    private Instant createdAt = Instant.now();

    public AiReport() {}

    public AiReport(String imagingTaskId, String consultationId, String findings,
                    String measurements, String impression, boolean criticalWarning) {
        this.imagingTaskId = imagingTaskId;
        this.consultationId = consultationId;
        this.findings = findings;
        this.measurements = measurements;
        this.impression = impression;
        this.criticalWarning = criticalWarning;
    }

    public String getId() { return id; }
    public String getImagingTaskId() { return imagingTaskId; }
    public String getConsultationId() { return consultationId; }
    public String getFindings() { return findings; }
    public String getMeasurements() { return measurements; }
    public String getImpression() { return impression; }
    public boolean isCriticalWarning() { return criticalWarning; }
    public Instant getCreatedAt() { return createdAt; }
}
