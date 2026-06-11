package com.cloudbrain.admin.application;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    public Map<String, Object> createSchedule(Map<String, Object> req) {
        return Map.of("status", "created", "scheduleId", "sched-" + System.currentTimeMillis());
    }

    public List<Map<String, String>> listDoctors() {
        return List.of(
                Map.of("id", "d-001", "name", "张医生", "dept", "DEPT-001", "role", "DOCTOR"),
                Map.of("id", "d-002", "name", "李医生", "dept", "DEPT-002", "role", "DOCTOR"));
    }

    public Map<String, Object> updatePermissions(String doctorId, Map<String, Object> req) {
        return Map.of("doctorId", doctorId, "status", "updated",
                "newPermissions", req.getOrDefault("permissions", List.of()));
    }
}
