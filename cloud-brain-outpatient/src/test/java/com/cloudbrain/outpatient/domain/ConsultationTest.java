package com.cloudbrain.outpatient.domain;

import com.cloudbrain.outpatient.domain.entity.Consultation;
import com.cloudbrain.outpatient.domain.vo.PatientInfo;
import com.cloudbrain.shared.enums.ConsultationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsultationTest {

    private Consultation consultation;

    @BeforeEach
    void setUp() {
        PatientInfo patient = new PatientInfo("p-1", "张三", "M", 35);
        consultation = new Consultation("d-1", "李医生", patient);
    }

    @Test
    void shouldCreateWithWaitingStatus() {
        assertEquals(ConsultationStatus.WAITING, consultation.getStatus());
    }

    @Test
    void shouldStartFromWaiting() {
        consultation.start();
        assertEquals(ConsultationStatus.IN_PROGRESS, consultation.getStatus());
    }

    @Test
    void shouldNotStartIfNotWaiting() {
        consultation.start();
        assertThrows(IllegalStateException.class, consultation::start);
    }

    @Test
    void shouldNotStartIfAlreadyFinished() {
        consultation.start();
        consultation.finish();
        assertThrows(IllegalStateException.class, consultation::start);
    }

    @Test
    void shouldFinishFromInProgress() {
        consultation.start();
        consultation.finish();
        assertEquals(ConsultationStatus.FINISHED, consultation.getStatus());
    }

    @Test
    void shouldNotFinishFromWaiting() {
        assertThrows(IllegalStateException.class, consultation::finish);
    }

    @Test
    void shouldPassFromInProgress() {
        consultation.start();
        consultation.pass();
        assertEquals(ConsultationStatus.PASSED, consultation.getStatus());
    }

    @Test
    void shouldEmitDomainEvents() {
        consultation.start();
        assertFalse(consultation.getDomainEvents().isEmpty());
    }

    @Test
    void patientInfoShouldBeAccessible() {
        assertEquals("p-1", consultation.getPatientInfo().getPatientId());
        assertEquals("张三", consultation.getPatientInfo().getPatientName());
    }
}
