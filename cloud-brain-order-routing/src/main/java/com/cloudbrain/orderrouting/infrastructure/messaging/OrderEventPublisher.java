package com.cloudbrain.orderrouting.infrastructure.messaging;

import com.cloudbrain.shared.event.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Outbox-based domain event publisher for the order-routing service.
 * <p>Serializes events to JSON and inserts them into the {@code order_outbox}
 * table within the caller's transaction, ensuring exactly-once persistence
 * alongside the aggregate write.</p>
 */
@Component
public class OrderEventPublisher {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventPublisher(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Persists the given domain event to the outbox table.
     * The event will be asynchronously forwarded to Kafka by the OutboxPoller.
     */
    public void publish(DomainEvent event) {
        try {
            String topic = resolveTopic(event);
            String payload = objectMapper.writeValueAsString(event);
            jdbcTemplate.update(
                    "INSERT INTO order_outbox (event_id, topic, payload, created_at) VALUES (?, ?, ?, NOW())",
                    event.getEventId(), topic, payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }

    private String resolveTopic(DomainEvent event) {
        String className = event.getClass().getSimpleName();
        if (className.contains("Order")) return "medical-order-events";
        return "medical-order-events";
    }
}
