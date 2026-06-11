package com.cloudbrain.outpatient.infrastructure.messaging;

import com.cloudbrain.shared.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsultationEventPublisher {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public ConsultationEventPublisher(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(DomainEvent event) {
        try {
            String topic = resolveTopic(event);
            String payload = objectMapper.writeValueAsString(event);
            jdbcTemplate.update(
                    "INSERT INTO outpatient_outbox (event_id, topic, payload, created_at) VALUES (?, ?, ?, NOW())",
                    event.getEventId(), topic, payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }

    private String resolveTopic(DomainEvent event) {
        String className = event.getClass().getSimpleName();
        if (className.contains("Consultation")) return "outpatient-consultation-events";
        return "outpatient-consultation-events";
    }
}
