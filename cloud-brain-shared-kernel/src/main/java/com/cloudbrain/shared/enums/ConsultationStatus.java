package com.cloudbrain.shared.enums;

/**
 * 就诊状态。
 * <pre>
 *   WAITING     → 已挂号，待医生叫号
 *   IN_PROGRESS → 就诊中（医生已接诊）
 *   PASSED      → 过号（患者未到场）
 *   FINISHED    → 就诊正常结束
 * </pre>
 */
public enum ConsultationStatus {
    WAITING,
    IN_PROGRESS,
    PASSED,
    FINISHED
}
