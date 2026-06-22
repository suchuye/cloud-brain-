package com.cloudbrain.imaging.infrastructure.messaging;

import com.cloudbrain.shared.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Outbox-based domain event publisher for the imaging service.
 * <p>Serializes events to JSON and inserts them into the {@code imaging_outbox}
 * table within the caller's transaction, ensuring exactly-once persistence
 * alongside the aggregate write.</p>
 */
@Component
public class ImagingEventPublisher {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public ImagingEventPublisher(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Persists the given domain event to the outbox table.
     * The event will be asynchronously forwarded to Kafka by the OutboxPoller.
     */
    public void publish(DomainEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            jdbcTemplate.update(
                    "INSERT INTO imaging_outbox (event_id, topic, payload, created_at) VALUES (?, ?, ?, NOW())",
                    event.getEventId(), "medical-imaging-events", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }
}
