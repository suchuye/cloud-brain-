package com.cloudbrain.aiassistant.domain.repository;

import com.cloudbrain.aiassistant.domain.entity.DiagnosticSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DiagnosticSessionRepository extends JpaRepository<DiagnosticSession, String> {
    Optional<DiagnosticSession> findByConsultationIdAndActiveTrue(String consultationId);
}
