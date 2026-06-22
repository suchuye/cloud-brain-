package com.cloudbrain.aiassistant.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diagnostic_session")
public class DiagnosticSession {

    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 36, nullable = false)
    private String consultationId;

    @Column(length = 36, nullable = false)
    private String doctorId;

    @Column(columnDefinition = "TEXT")
    private String dialogHistory;

    @Column(columnDefinition = "TEXT")
    private String suggestions;

    private boolean active = true;
    private Instant createdAt;
    private Instant endedAt;

    public DiagnosticSession() {}

    public DiagnosticSession(String consultationId, String doctorId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.consultationId = consultationId;
        this.doctorId = doctorId;
        this.dialogHistory = "[]";
        this.suggestions = "[]";
        this.active = true;
        this.createdAt = Instant.now();
    }

    public void addDialog(String text) {
        var list = getDialogHistoryList();
        list.add(text);
        this.dialogHistory = toJson(list);
    }

    public void addSuggestion(String suggestion) {
        var list = getSuggestionsList();
        list.add(suggestion);
        this.suggestions = toJson(list);
    }

    public void end() { this.active = false; this.endedAt = Instant.now(); }

    public List<String> getDialogHistoryList() {
        return fromJson(this.dialogHistory);
    }

    public List<String> getSuggestionsList() {
        return fromJson(this.suggestions);
    }

    public String buildSummary() {
        return String.format("""
                {"consultationId":"%s","dialogCount":%d,"suggestions":%s,\
                "duration":"%s"}""",
                consultationId, getDialogHistoryList().size(), suggestions, "unknown");
    }

    // --- getters/setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getDialogHistory() { return dialogHistory; }
    public void setDialogHistory(String dialogHistory) { this.dialogHistory = dialogHistory; }
    public String getSuggestions() { return suggestions; }
    public void setSuggestions(String suggestions) { this.suggestions = suggestions; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getEndedAt() { return endedAt; }
    public void setEndedAt(Instant endedAt) { this.endedAt = endedAt; }

    private List<String> fromJson(String json) {
        if (json == null || json.isEmpty() || json.equals("[]")) return new ArrayList<>();
        var list = new ArrayList<String>();
        var s = json.substring(1, json.length() - 1);
        if (s.isEmpty()) return list;
        for (var part : s.split("\",\"")) {
            var clean = part;
            if (clean.startsWith("\"")) clean = clean.substring(1);
            if (clean.endsWith("\"")) clean = clean.substring(0, clean.length() - 1);
            list.add(clean);
        }
        return list;
    }

    private String toJson(List<String> list) {
        var sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(list.get(i).replace("\"", "\\\"").replace("\n", "\\n")).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }
}
