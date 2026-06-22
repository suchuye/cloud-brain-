package com.cloudbrain.shared.event;

import java.time.Instant;
import java.util.UUID;

/**
 * 领域事件基类，所有跨服务事件均继承此类。
 * 携带全局唯一 eventId 和发生时间戳，
 * 消费者可据此做幂等去重。
 */
public abstract class DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredAt = Instant.now();

    /** 全局唯一事件 ID，用于幂等消费 */
    public String getEventId() { return eventId; }

    /** 事件发生时间 */
    public Instant getOccurredAt() { return occurredAt; }
}
