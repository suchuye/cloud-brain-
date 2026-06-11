package com.cloudbrain.aiassistant.infrastructure.messaging;

import com.cloudbrain.shared.event.DomainEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AiEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AiEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(DomainEvent event) {
        kafkaTemplate.send("ai-assistant-events", event.getEventId(),
                eventToJson(event));
    }

    private String eventToJson(DomainEvent event) {
        // 简单序列化，实际应用中使用 ObjectMapper
        return "{\"eventId\":\"" + event.getEventId() + "\"}";
    }
}
