package com.cloudbrain.aiassistant.application;

import com.cloudbrain.aiassistant.domain.entity.DiagnosticSession;
import com.cloudbrain.aiassistant.infrastructure.messaging.AiEventPublisher;
import com.cloudbrain.shared.event.ConsultationAiSummaryEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AiAssistantService {

    private final Map<String, DiagnosticSession> sessions = new ConcurrentHashMap<>();
    private final AiEventPublisher eventPublisher;

    public AiAssistantService(AiEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public DiagnosticSession startSession(String consultationId, String doctorId) {
        DiagnosticSession session = new DiagnosticSession(consultationId, doctorId);
        sessions.put(session.getId(), session);
        return session;
    }

    public String processText(String sessionId, String text) {
        DiagnosticSession session = sessions.get(sessionId);
        if (session == null) throw new RuntimeException("Session not found");

        session.addDialog("doctor: " + text);

        // 模拟 LLM 推理
        String suggestion = analyze(text);
        session.addSuggestion(suggestion);

        return suggestion;
    }

    public DiagnosticSession endSession(String sessionId) {
        DiagnosticSession session = sessions.get(sessionId);
        if (session == null) throw new RuntimeException("Session not found");

        session.end();
        sessions.remove(sessionId);

        // 异步归档摘要
        eventPublisher.publish(new ConsultationAiSummaryEvent(
                session.getConsultationId(), session.buildSummary()));

        return session;
    }

    private String analyze(String text) {
        if (text.contains("头痛") || text.contains("头疼")) {
            return "考虑偏头痛或紧张性头痛，建议询问发作频率、持续时间及伴随症状";
        }
        if (text.contains("发热") || text.contains("发烧")) {
            return "考虑感染性疾病，建议完善血常规、CRP，排查呼吸道/泌尿道感染";
        }
        if (text.contains("胸痛") || text.contains("胸闷")) {
            return "⚠️ 警惕急性冠脉综合征，建议立即心电图 + 肌钙蛋白";
        }
        if (text.contains("咳嗽")) {
            return "考虑呼吸道感染或过敏，建议听诊双肺呼吸音，必要时胸部 X 线";
        }
        return "已记录，建议进一步询问伴随症状及病程";
    }
}
