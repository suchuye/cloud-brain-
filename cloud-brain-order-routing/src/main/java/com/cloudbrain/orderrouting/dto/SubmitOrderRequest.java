package com.cloudbrain.orderrouting.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for submitting a medical order.
 * <p>Carries the idempotency key, consultation reference, order type, and optional
 * JSON details body.</p>
 */
public record SubmitOrderRequest(
        @NotBlank String idempotencyKey,
        @NotBlank String consultationId,
        @NotBlank String orderType,
        String orderDetails) {}
