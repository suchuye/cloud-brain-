package com.cloudbrain.scheduling.domain.vo;

public enum QueueStatus {
    WAITING,          // 已挂号，等待签到
    IN_QUEUE,         // 已签到，排队中
    IN_CONSULTATION,  // 就诊中（医生叫号后）
    COMPLETED,        // 正常结束
    PASSED,           // 过号
    CANCELLED         // 取消
}
