package com.cloudbrain.patient.application;

import com.cloudbrain.patient.dto.BookRequest;
import com.cloudbrain.patient.dto.TriageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PatientService {

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

    public Map<String, String> book(BookRequest req) {
        // 实际应调用 Scheduling 服务创建队列记录
        return Map.of("status", "booked",
                "patientId", req.patientId(),
                "departmentId", req.departmentId(),
                "queueNumber", String.valueOf(System.currentTimeMillis() % 100));
    }

    public Object getReports(String patientId) {
        // 实际应查询 Imaging 服务
        return Map.of("patientId", patientId, "reports", java.util.List.of());
    }
}
