package com.cloudbrain.aiassistant.application;

import com.cloudbrain.aiassistant.domain.entity.DiagnosticSession;
import com.cloudbrain.aiassistant.infrastructure.messaging.AiEventPublisher;
import com.cloudbrain.shared.event.ConsultationAiSummaryEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AiAssistantService {

    private static final Logger log = LoggerFactory.getLogger(AiAssistantService.class);
    private static final String LLM_URL = "http://localhost:8089/api/llm";

    private final Map<String, DiagnosticSession> sessions = new ConcurrentHashMap<>();
    private final AiEventPublisher eventPublisher;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public AiAssistantService(AiEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public DiagnosticSession startSession(String consultationId, String doctorId) {
        DiagnosticSession session = new DiagnosticSession(consultationId, doctorId);
        sessions.put(session.getId(), session);
        log.info("AI session started: consultation={}, doctor={}", consultationId, doctorId);
        return session;
    }

    public String processText(String sessionId, String text) {
        DiagnosticSession session = sessions.get(sessionId);
        if (session == null) throw new RuntimeException("Session not found");
        session.addDialog("医生: " + text);

        String dialogHistory = String.join("\n", session.getDialogHistory());
        String suggestion = callLlm("diagnose", dialogHistory);
        session.addSuggestion(suggestion);
        return suggestion;
    }

    public DiagnosticSession endSession(String sessionId) {
        DiagnosticSession session = sessions.get(sessionId);
        if (session == null) throw new RuntimeException("Session not found");
        session.end();
        sessions.remove(sessionId);

        String dialogHistory = String.join("\n", session.getDialogHistory());
        String summary = callLlm("summarize", dialogHistory);
        eventPublisher.publish(new ConsultationAiSummaryEvent(session.getConsultationId(), summary));

        log.info("AI session ended: consultation={}, dialogs={}",
                session.getConsultationId(), session.getDialogHistory().size());
        return session;
    }

    private String callLlm(String endpoint, String dialog) {
        try {
            var body = mapper.writeValueAsString(Map.of("dialog", dialog));
            var req = HttpRequest.newBuilder()
                    .uri(URI.create(LLM_URL + "/" + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                var tree = mapper.readTree(resp.body());
                return tree.get("result").asText("LLM 未返回结果");
            }
        } catch (Exception e) {
            log.warn("LLM call failed, using fallback", e);
        }
        return "【鉴别诊断】\nLLM 服务暂不可用\n【建议检查】\n根据临床需要完善相关检查";
    }
}
