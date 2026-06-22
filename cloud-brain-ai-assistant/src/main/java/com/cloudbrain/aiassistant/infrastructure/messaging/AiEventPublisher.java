package com.cloudbrain.aiassistant.infrastructure.messaging;

import com.cloudbrain.shared.event.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AiEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AiEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public AiEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(DomainEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            kafkaTemplate.send("ai-assistant-events", event.getEventId(), json);
            log.info("Published {} to ai-assistant-events", event.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}", event.getEventId(), e);
        }
    }
}
