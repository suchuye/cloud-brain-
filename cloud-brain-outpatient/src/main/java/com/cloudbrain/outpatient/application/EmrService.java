package com.cloudbrain.outpatient.application;

import com.cloudbrain.outpatient.domain.entity.Emr;
import com.cloudbrain.outpatient.domain.entity.Consultation;
import com.cloudbrain.outpatient.domain.repository.ConsultationRepository;
import com.cloudbrain.outpatient.infrastructure.search.EmrDocument;
import com.cloudbrain.outpatient.infrastructure.search.EmrSearchService;
import jakarta.persistence.EntityManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service for managing EMR records.
 * Persists EMR data and asynchronously indexes it into Elasticsearch for full-text search.
 */
@Service
public class EmrService {

    private final EntityManager em;
    private final ConsultationRepository consultationRepository;
    private final EmrSearchService searchService;

    public EmrService(EntityManager em, ConsultationRepository consultationRepository,
                      EmrSearchService searchService) {
        this.em = em;
        this.consultationRepository = consultationRepository;
        this.searchService = searchService;
    }

    /**
     * Persists a new EMR for the given consultation and asynchronously indexes it in Elasticsearch.
     */
    @Transactional
    public Emr saveEmr(String consultationId, String content) {
        Emr emr = new Emr(consultationId, content);
        em.persist(emr);
        em.flush();  // 确保 ID 生成

        indexAsync(emr, consultationId, content);
        return emr;
    }

    /**
     * Updates an existing EMR's content and re-indexes it in Elasticsearch.
     */
    @Transactional
    public Emr updateEmr(String emrId, String content) {
        Emr emr = em.find(Emr.class, emrId);
        if (emr == null) throw new RuntimeException("EMR not found");
        emr.updateContent(content);
        em.merge(emr);

        indexAsync(emr, emr.getConsultationId(), content);
        return emr;
    }

    /**
     * Searches EMR records by keyword across chief complaint and present illness fields.
     */
    public List<EmrDocument> search(String keyword) {
        return searchService.searchByKeyword(keyword);
    }

    /**
     * Searches EMR history for a specific patient by patient ID.
     */
    public List<EmrDocument> searchByPatient(String patientId) {
        return searchService.searchByPatient(patientId);
    }

    private void indexAsync(Emr emr, String consultationId, String content) {
        new Thread(() -> {
            try {
                Consultation c = consultationRepository.findById(consultationId).orElse(null);
                if (c == null) return;
                EmrDocument doc = new EmrDocument(
                        emr.getId(), consultationId,
                        c.getDoctorId(), c.getPatientInfo().getPatientId(),
                        extractField(content, "chiefComplaint"),
                        extractField(content, "presentIllness"),
                        content);
                searchService.index(doc);
            } catch (Exception ignored) {
                // ES indexing failure should not block EMR save
            }
        }).start();
    }

    private String extractField(String json, String field) {
        int start = json.indexOf("\"" + field + "\"");
        if (start < 0) return "";
        start = json.indexOf(":", start) + 1;
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        if (end < 0) return "";
        return json.substring(start, end).trim().replace("\"", "");
    }
}
