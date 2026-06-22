package com.cloudbrain.aiassistant.application;

import com.cloudbrain.aiassistant.domain.entity.DiagnosticSession;
import com.cloudbrain.aiassistant.domain.repository.DiagnosticSessionRepository;
import com.cloudbrain.aiassistant.infrastructure.messaging.AiEventPublisher;
import com.cloudbrain.shared.event.ConsultationAiSummaryEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
public class AiAssistantService {

    private static final Logger log = LoggerFactory.getLogger(AiAssistantService.class);

    private final DiagnosticSessionRepository repository;
    private final AiEventPublisher eventPublisher;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String llmUrl;

    public AiAssistantService(DiagnosticSessionRepository repository,
                              AiEventPublisher eventPublisher,
                              @Value("${ai.llm.url:http://cloud-brain-ai-llm/api/llm}") String llmUrl) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.llmUrl = llmUrl;
    }

    @Transactional
    public DiagnosticSession getOrCreateSession(String consultationId, String doctorId) {
        return repository.findByConsultationIdAndActiveTrue(consultationId)
                .map(session -> {
                    log.info("Rejoined session {} for consultation {}", session.getId(), consultationId);
                    return session;
                })
                .orElseGet(() -> {
                    DiagnosticSession session = new DiagnosticSession(consultationId, doctorId);
                    session = repository.save(session);
                    log.info("Created session {} for consultation {} doctor {}", session.getId(), consultationId, doctorId);
                    return session;
                });
    }

    @Transactional
    public String processText(String sessionId, String text) {
        DiagnosticSession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
        session.addDialog("医生: " + text);
        repository.save(session);

        String dialogHistory = String.join("\n", session.getDialogHistoryList());
        String suggestion = callLlm("diagnose", dialogHistory);
        session.addSuggestion(suggestion);
        repository.save(session);

        return suggestion;
    }

    @Transactional
    public DiagnosticSession endSession(String sessionId) {
        DiagnosticSession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found: " + sessionId));
        session.end();
        repository.save(session);

        String dialogHistory = String.join("\n", session.getDialogHistoryList());
        String summary = callLlm("summarize", dialogHistory);
        eventPublisher.publish(new ConsultationAiSummaryEvent(session.getConsultationId(), summary));

        log.info("Session ended: consultation={}, dialogs={}",
                session.getConsultationId(), session.getDialogHistoryList().size());
        return session;
    }

    @Transactional
    public void markDisconnected(String sessionId) {
        repository.findById(sessionId).ifPresent(session -> {
            session.setActive(false);
            repository.save(session);
            log.info("Session {} marked disconnected", sessionId);
        });
    }

    private String callLlm(String endpoint, String dialog) {
        try {
            var body = mapper.writeValueAsString(Map.of("dialog", dialog));
            var req = HttpRequest.newBuilder()
                    .uri(URI.create(llmUrl + "/" + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            var resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                var tree = mapper.readTree(resp.body());
                return tree.get("result").asText("LLM 未返回结果");
            }
            log.error("LLM returned HTTP {}", resp.statusCode());
        } catch (Exception e) {
            log.warn("LLM call failed, using fallback", e);
        }
        return """
                【鉴别诊断】
                LLM 服务暂不可用
                【建议检查】
                根据临床需要完善相关检查""";
    }
}
