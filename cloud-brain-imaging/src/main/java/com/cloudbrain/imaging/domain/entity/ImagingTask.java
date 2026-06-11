package com.cloudbrain.imaging.domain.entity;

import com.cloudbrain.imaging.domain.vo.ImagingTaskStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "imaging_task")
public class ImagingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String consultationId;
    private String orderId;

    private String modality;     // CT, MRI, XRAY
    private String bodyPart;     // HEAD, CHEST, ABDOMEN

    private String filePath;     // DICOM/NIfTI 文件路径
    private String fileMd5;      // 文件完整性校验

    @Enumerated(EnumType.STRING)
    private ImagingTaskStatus status = ImagingTaskStatus.RECEIVED;

    private String errorMessage;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public ImagingTask() {}

    public ImagingTask(String consultationId, String orderId, String modality, String bodyPart,
                       String filePath, String fileMd5) {
        this.consultationId = consultationId;
        this.orderId = orderId;
        this.modality = modality;
        this.bodyPart = bodyPart;
        this.filePath = filePath;
        this.fileMd5 = fileMd5;
    }

    public void startPreprocessing() {
        if (status != ImagingTaskStatus.RECEIVED) throw new IllegalStateException("Not received");
        status = ImagingTaskStatus.PREPROCESSING;
        updatedAt = Instant.now();
    }

    public void startInference() {
        if (status != ImagingTaskStatus.PREPROCESSING) throw new IllegalStateException("Not preprocessed");
        status = ImagingTaskStatus.INFERRING;
        updatedAt = Instant.now();
    }

    public void complete() {
        status = ImagingTaskStatus.COMPLETED;
        updatedAt = Instant.now();
    }

    public void fail(String message) {
        status = ImagingTaskStatus.FAILED;
        this.errorMessage = message;
        updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getConsultationId() { return consultationId; }
    public String getOrderId() { return orderId; }
    public String getModality() { return modality; }
    public String getBodyPart() { return bodyPart; }
    public String getFilePath() { return filePath; }
    public String getFileMd5() { return fileMd5; }
    public ImagingTaskStatus getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
