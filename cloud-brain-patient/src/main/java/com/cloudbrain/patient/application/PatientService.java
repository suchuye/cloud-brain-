package com.cloudbrain.patient.application;

import com.cloudbrain.patient.dto.BookRequest;
import com.cloudbrain.patient.dto.TriageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Application service for patient-facing operations.
 * Provides symptom-based triage (mapping symptoms to departments), appointment
 * booking (delegated to the Scheduling service), and imaging report retrieval.
 */
@Service
public class PatientService {

    /**
     * Maps a symptom keyword to a recommended department and urgency level.
     * Uses simple keyword matching; chest pain is flagged as urgent.
     */
    public Map<String, String> triage(TriageRequest req) {
        String symptom = req.symptom().toLowerCase();
        String department;

        if (symptom.contains("头痛") || symptom.contains("头晕")) department = "神经内科";
        else if (symptom.contains("胸痛") || symptom.contains("心慌")) department = "心内科";
        else if (symptom.contains("咳嗽") || symptom.contains("发热")) department = "呼吸内科";
        else if (symptom.contains("腹痛") || symptom.contains("腹泻")) department = "消化内科";
        else if (symptom.contains("骨折") || symptom.contains("外伤")) department = "骨科";
        else department = "全科门诊";

        return Map.of("symptom", req.symptom(), "recommendedDept", department,
                "urgency", symptom.contains("胸痛") ? "紧急" : "普通");
    }

    /**
     * Books an appointment for the patient in the specified department.
     * Currently returns a simulated queue number; should delegate to the Scheduling service.
     */
    public Map<String, String> book(BookRequest req) {
        // 实际应调用 Scheduling 服务创建队列记录
        return Map.of("status", "booked",
                "patientId", req.patientId(),
                "departmentId", req.departmentId(),
                "queueNumber", String.valueOf(System.currentTimeMillis() % 100));
    }

    /**
     * Retrieves imaging reports for the given patient.
     * Currently returns an empty list; should delegate to the Imaging service.
     */
    public Object getReports(String patientId) {
        // 实际应查询 Imaging 服务
        return Map.of("patientId", patientId, "reports", java.util.List.of());
    }
}
