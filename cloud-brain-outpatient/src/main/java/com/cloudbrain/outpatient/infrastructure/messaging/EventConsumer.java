package com.cloudbrain.outpatient.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer handling cross-module events for the outpatient workflow.
 * Listens for medical imaging reports, medical orders, and doctor scheduling changes.
 */
@Component
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "medical-imaging-events", groupId = "outpatient")
    public void onCtReport(String message) {
        log.info("[影像] CT报告已生成，通知医生查阅: consultationId={}", extractField(message, "consultationId"));
    }

    @KafkaListener(topics = "medical-order-events", groupId = "outpatient")
    public void onOrderEvent(String message) {
        String orderId = extractField(message, "orderId");
        String type = extractField(message, "orderType");
        log.info("[医嘱] 医嘱已路由: orderId={}, type={}, 等待下游执行完成", orderId, type);
    }

    @KafkaListener(topics = "doctor-scheduling-events", groupId = "outpatient")
    public void onQueueUpdate(String message) {
        String changeType = extractField(message, "changeType");
        String doctorId = extractField(message, "doctorId");
        log.info("[队列] 队列变更: doctorId={}, changeType={}, 刷新候诊列表", doctorId, changeType);
    }

    private String extractField(String json, String field) {
        int start = json.indexOf("\"" + field + "\":");
        if (start < 0) return "?";
        start += field.length() + 3;
        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return end > start ? json.substring(start, end) : "?";
        }
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        return json.substring(start, end).trim();
    }
}
