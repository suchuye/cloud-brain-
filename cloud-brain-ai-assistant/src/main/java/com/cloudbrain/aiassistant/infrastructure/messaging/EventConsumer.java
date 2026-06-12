package com.cloudbrain.aiassistant.infrastructure.messaging;

import com.cloudbrain.aiassistant.application.AiAssistantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);
    private final AiAssistantService aiService;

    public EventConsumer(AiAssistantService aiService) {
        this.aiService = aiService;
    }

    @KafkaListener(topics = "outpatient-consultation-events", groupId = "ai-assistant")
    public void onConsultationStarted(String message) {
        log.info("👂 接诊开始，初始化 AI 会话: {}", truncate(message));

        try {
            // 从消息中提取 consultationId, doctorId（简化处理）
            String consultationId = extractField(message, "consultationId");
            String doctorId = extractField(message, "doctorId");
            if (consultationId != null && doctorId != null) {
                aiService.startSession(consultationId, doctorId);
                log.info("✅ AI 会话已就绪: consultation={}", consultationId);
            }
        } catch (Exception e) {
            log.warn("初始化 AI 会话失败，等待 WebSocket 手动触发", e);
        }
    }

    private String extractField(String json, String field) {
        int start = json.indexOf("\"" + field + "\"");
        if (start < 0) return null;
        start = json.indexOf(":", start) + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return end > start ? json.substring(start, end) : null;
        }
        return null;
    }

    private String truncate(String msg) {
        return msg.length() > 120 ? msg.substring(0, 120) + "..." : msg;
    }
}
