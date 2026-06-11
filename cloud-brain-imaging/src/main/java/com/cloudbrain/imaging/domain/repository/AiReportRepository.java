package com.cloudbrain.imaging.domain.repository;

import com.cloudbrain.imaging.domain.entity.AiReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiReportRepository extends JpaRepository<AiReport, String> {
    Optional<AiReport> findByImagingTaskId(String imagingTaskId);
}
