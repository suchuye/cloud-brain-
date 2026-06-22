package com.cloudbrain.imaging.application;

import com.cloudbrain.imaging.domain.entity.AiReport;
import com.cloudbrain.imaging.domain.entity.ImagingTask;
import com.cloudbrain.imaging.domain.repository.AiReportRepository;
import com.cloudbrain.imaging.domain.repository.ImagingTaskRepository;
import com.cloudbrain.imaging.dto.SubmitImageRequest;
import com.cloudbrain.imaging.infrastructure.messaging.ImagingEventPublisher;
import com.cloudbrain.shared.event.CtReportGeneratedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service that orchestrates the imaging analysis pipeline.
 * <p>Handles image submission, task lifecycle progression (preprocessing,
 * inference, report generation), and outbox event publishing.</p>
 */
@Service
public class ImagingService {

    private final ImagingTaskRepository taskRepository;
    private final AiReportRepository reportRepository;
    private final ImagingEventPublisher eventPublisher;

    public ImagingService(ImagingTaskRepository taskRepository,
                          AiReportRepository reportRepository,
                          ImagingEventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.reportRepository = reportRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Submits a new imaging task from the provided image metadata and
     * triggers the processing pipeline inline (preprocessing, inference,
     * report generation).
     */
    @Transactional
    public ImagingTask submitImage(SubmitImageRequest req) {
        ImagingTask task = new ImagingTask(req.consultationId(), req.orderId(),
                req.modality(), req.bodyPart(), req.filePath(), req.fileMd5());
        task = taskRepository.save(task);

        // 模拟异步 AI 处理流程（实际应为消息驱动或异步线程）
        processTask(task);

        return task;
    }

    /** Retrieves an imaging task by its ID. Throws if not found. */
    public ImagingTask getTask(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /** Retrieves the AI report associated with the given task ID. Throws if not found. */
    public AiReport getReport(String taskId) {
        return reportRepository.findByImagingTaskId(taskId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    /**
     * Executes the full processing pipeline for an imaging task:
     * preprocessing -> AI inference -> report generation -> event publishing.
     * Each step persists state changes transactionally.
     */
    @Transactional
    public void processTask(ImagingTask task) {
        // 预处理
        task.startPreprocessing();
        taskRepository.save(task);

        // AI 推理
        task.startInference();
        taskRepository.save(task);

        // 生成报告（模拟）
        String findings = """
                {"nodules":[{"location":"右肺上叶","size":"8mm","character":"磨玻璃样"}],\
                "abnormalities":[]}""";
        String measurements = """
                {"nodule_01":{"diameter_mm":8,"volume_mm3":256,"density_HU":-650}}""";
        String impression = "右肺上叶磨玻璃结节，建议 6 个月随访复查";

        AiReport report = new AiReport(task.getId(), task.getConsultationId(),
                findings, measurements, impression, false);
        reportRepository.save(report);

        task.complete();
        taskRepository.save(task);

        // 发布事件
        eventPublisher.publish(new CtReportGeneratedEvent(
                task.getId(), task.getConsultationId(), report.getId(), report.isCriticalWarning()));
    }
}
