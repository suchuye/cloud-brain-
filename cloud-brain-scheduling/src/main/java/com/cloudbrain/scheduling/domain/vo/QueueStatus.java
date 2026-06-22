package com.cloudbrain.scheduling.domain.vo;

/**
 * State machine for the patient queue lifecycle.
 * WAITING (registered) -> IN_QUEUE (checked in) -> IN_CONSULTATION (called) -> {COMPLETED, PASSED}.
 * CANCELLED is a terminal state reachable from any status except COMPLETED.
 */
public enum QueueStatus {
    WAITING,          // 已挂号，等待签到
    IN_QUEUE,         // 已签到，排队中
    IN_CONSULTATION,  // 就诊中（医生叫号后）
    COMPLETED,        // 正常结束
    PASSED,           // 过号
    CANCELLED         // 取消
}
