package com.cloudbrain.outpatient.domain.repository;

import com.cloudbrain.outpatient.domain.entity.Consultation;
import com.cloudbrain.shared.enums.ConsultationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, String> {
    List<Consultation> findByDoctorIdAndStatus(String doctorId, ConsultationStatus status);
    Optional<Consultation> findTopByDoctorIdAndStatusOrderByCreatedAtAsc(String doctorId, ConsultationStatus status);
}
