package com.cloudbrain.imaging.domain.repository;

import com.cloudbrain.imaging.domain.entity.ImagingTask;
import com.cloudbrain.imaging.domain.vo.ImagingTaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the ImagingTask aggregate root.
 * <p>Supports consultation-scoped and status-scoped queries for task
 * tracking and monitoring.</p>
 */
public interface ImagingTaskRepository extends JpaRepository<ImagingTask, String> {
    List<ImagingTask> findByConsultationId(String consultationId);
    List<ImagingTask> findByStatus(ImagingTaskStatus status);
}
