package com.cloudbrain.imaging.domain.vo;

/**
 * Lifecycle states of an imaging processing task.
 * <p>State machine: RECEIVED -> PREPROCESSING -> INFERRING -> COMPLETED.
 * Any active state may transition to FAILED on error.</p>
 */
public enum ImagingTaskStatus {
    RECEIVED,
    PREPROCESSING,
    INFERRING,
    COMPLETED,
    FAILED
}
