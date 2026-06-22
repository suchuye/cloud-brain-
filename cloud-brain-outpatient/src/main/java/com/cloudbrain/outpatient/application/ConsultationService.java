package com.cloudbrain.outpatient.application;

import com.cloudbrain.outpatient.domain.entity.Consultation;
import com.cloudbrain.outpatient.domain.repository.ConsultationRepository;
import com.cloudbrain.outpatient.dto.StartConsultationRequest;
import com.cloudbrain.outpatient.infrastructure.messaging.ConsultationEventPublisher;
import com.cloudbrain.shared.event.ConsultationStartedEvent;
import com.cloudbrain.shared.event.ConsultationFinishedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service orchestrating consultation lifecycle operations.
 * Coordinates domain entities, persistence, and integration event publishing via the outbox.
 */
@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ConsultationEventPublisher eventPublisher;

    public ConsultationService(ConsultationRepository consultationRepository,
                               ConsultationEventPublisher eventPublisher) {
        this.consultationRepository = consultationRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Starts a consultation by transitioning it to IN_PROGRESS and publishing a ConsultationStartedEvent.
     */
    @Transactional
    public void startConsultation(StartConsultationRequest request) {
        Consultation consultation = consultationRepository.findById(request.consultationId())
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        consultation.start();

        consultationRepository.save(consultation);

        // Publish integration event via outbox
        eventPublisher.publish(new ConsultationStartedEvent(
                consultation.getId(),
                consultation.getDoctorId(),
                consultation.getPatientInfo().getPatientId()));
    }

    /**
     * Finishes a consultation by transitioning it to FINISHED and publishing a ConsultationFinishedEvent.
     */
    @Transactional
    public void finishConsultation(String consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        consultation.finish();

        consultationRepository.save(consultation);

        eventPublisher.publish(new ConsultationFinishedEvent(consultation.getId()));
    }
}
