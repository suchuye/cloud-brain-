package com.cloudbrain.aiassistant.infrastructure.messaging;

import com.cloudbrain.aiassistant.application.AiAssistantService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);
    private final AiAssistantService aiService;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventConsumer(AiAssistantService aiService) {
        this.aiService = aiService;
    }

    @KafkaListener(topics = "outpatient-consultation-events", groupId = "ai-assistant")
    public void onConsultationStarted(String message) {
        log.info("Received consultation event: {}", truncate(message));
        try {
            JsonNode root = mapper.readTree(message);
            String consultationId = root.path("consultationId").asText(null);
            String doctorId = root.path("doctorId").asText(null);
            if (consultationId != null && doctorId != null) {
                aiService.getOrCreateSession(consultationId, doctorId);
                log.info("AI session ready: consultation={}", consultationId);
            }
        } catch (Exception e) {
            log.warn("Failed to init AI session from Kafka, will be created on WebSocket connect", e);
        }
    }

    private String truncate(String msg) {
        return msg.length() > 120 ? msg.substring(0, 120) + "..." : msg;
    }
}
