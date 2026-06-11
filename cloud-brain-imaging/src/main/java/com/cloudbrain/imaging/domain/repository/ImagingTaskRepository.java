package com.cloudbrain.imaging.domain.repository;

import com.cloudbrain.imaging.domain.entity.ImagingTask;
import com.cloudbrain.imaging.domain.vo.ImagingTaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagingTaskRepository extends JpaRepository<ImagingTask, String> {
    List<ImagingTask> findByConsultationId(String consultationId);
    List<ImagingTask> findByStatus(ImagingTaskStatus status);
}
