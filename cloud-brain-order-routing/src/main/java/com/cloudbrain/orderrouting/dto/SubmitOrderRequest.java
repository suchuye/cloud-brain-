package com.cloudbrain.orderrouting.dto;

import jakarta.validation.constraints.NotBlank;

public record SubmitOrderRequest(
        @NotBlank String idempotencyKey,
        @NotBlank String consultationId,
        @NotBlank String orderType,
        String orderDetails) {}
