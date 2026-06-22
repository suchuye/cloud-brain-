package com.cloudbrain.imaging.domain.repository;

import com.cloudbrain.imaging.domain.entity.AiReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the AiReport entity.
 * <p>Supports lookup of reports by their associated imaging task ID.</p>
 */
public interface AiReportRepository extends JpaRepository<AiReport, String> {
    Optional<AiReport> findByImagingTaskId(String imagingTaskId);
}
