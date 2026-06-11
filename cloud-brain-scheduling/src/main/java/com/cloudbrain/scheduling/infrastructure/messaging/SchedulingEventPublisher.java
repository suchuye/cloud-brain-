package com.cloudbrain.scheduling.infrastructure.messaging;

import com.cloudbrain.shared.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchedulingEventPublisher {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public SchedulingEventPublisher(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            jdbcTemplate.update(
                    "INSERT INTO scheduling_outbox (event_id, topic, payload, created_at) VALUES (?, ?, ?, NOW())",
                    event.getEventId(), "doctor-scheduling-events", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }
}
