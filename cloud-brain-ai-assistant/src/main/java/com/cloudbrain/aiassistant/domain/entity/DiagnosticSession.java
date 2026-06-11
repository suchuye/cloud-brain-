package com.cloudbrain.aiassistant.domain.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiagnosticSession {

    private final String id = UUID.randomUUID().toString();
    private String consultationId;
    private String doctorId;
    private final List<String> dialogHistory = new ArrayList<>();  // 对话上下文
    private final List<String> suggestions = new ArrayList<>();    // AI 建议历史
    private boolean active = true;
    private final Instant createdAt = Instant.now();
    private Instant endedAt;

    public DiagnosticSession() {}

    public DiagnosticSession(String consultationId, String doctorId) {
        this.consultationId = consultationId;
        this.doctorId = doctorId;
    }

    public void addDialog(String text) { dialogHistory.add(text); }
    public void addSuggestion(String suggestion) { suggestions.add(suggestion); }
    public void end() { this.active = false; this.endedAt = Instant.now(); }

    public String getId() { return id; }
    public String getConsultationId() { return consultationId; }
    public String getDoctorId() { return doctorId; }
    public List<String> getDialogHistory() { return List.copyOf(dialogHistory); }
    public List<String> getSuggestions() { return List.copyOf(suggestions); }
    public boolean isActive() { return active; }

    public String buildSummary() {
        return String.format("""
                {"consultationId":"%s","dialogCount":%d,"suggestions":%s,\
                "duration":"%s"}""",
                consultationId, dialogHistory.size(), suggestions, "unknown");
    }
}
