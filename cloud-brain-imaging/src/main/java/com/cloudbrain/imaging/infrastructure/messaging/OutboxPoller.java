package com.cloudbrain.imaging.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Scheduled outbox table poller for the imaging service.
 * <p>Polls the {@code imaging_outbox} table every 500ms, sends unsent events
 * (retry_count < 3) to Kafka, and marks them as sent on success or increments
 * the retry counter on failure.</p>
 */
@Component
public class OutboxPoller {

    private static final Logger log = LoggerFactory.getLogger(OutboxPoller.class);
    private static final String TABLE = "imaging_outbox";

    private final JdbcTemplate jdbc;
    private final KafkaTemplate<String, String> kafka;

    public OutboxPoller(JdbcTemplate jdbc, KafkaTemplate<String, String> kafka) {
        this.jdbc = jdbc;
        this.kafka = kafka;
    }

    /**
     * Polls the outbox table for unsent events and forwards them to Kafka.
     * Events that fail to send are retried up to 3 times before being abandoned.
     */
    @Scheduled(fixedDelay = 500)
    public void poll() {
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT id, event_id, topic, payload FROM " + TABLE +
                " WHERE sent = 0 AND retry_count < 3 ORDER BY created_at LIMIT 100");

        for (Map<String, Object> row : rows) {
            Long id = (Long) row.get("id");
            String eventId = (String) row.get("event_id");
            String topic = (String) row.get("topic");
            String payload = (String) row.get("payload");

            try {
                kafka.send(topic, eventId, payload).get();
                jdbc.update("UPDATE " + TABLE + " SET sent = 1 WHERE id = ?", id);
            } catch (Exception e) {
                log.error("Outbox send failed: {}", eventId, e);
                jdbc.update("UPDATE " + TABLE + " SET retry_count = retry_count + 1 WHERE id = ?", id);
            }
        }
    }
}
