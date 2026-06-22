package com.cloudbrain.shared.enums;

/**
 * 医嘱状态。
 * <pre>
 *   PENDING   → 医生已提交，等待路由服务确认
 *   ACCEPTED  → 路由服务已持久化并确认接收（同步返回给门诊）
 *   ROUTED    → 已通过 Kafka 分发到下游（药房/检查科室）
 *   COMPLETED → 下游执行完毕
 *   REJECTED  → 下游驳回（库存不足/禁忌症等）
 * </pre>
 */
public enum OrderStatus {
    PENDING,
    ACCEPTED,
    ROUTED,
    COMPLETED,
    REJECTED
}
