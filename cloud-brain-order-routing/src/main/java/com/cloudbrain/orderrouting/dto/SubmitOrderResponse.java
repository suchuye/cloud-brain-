package com.cloudbrain.orderrouting.dto;

import com.cloudbrain.shared.enums.OrderStatus;

/**
 * Response payload returned after order submission.
 * <p>Contains the generated order ID and its current lifecycle status.</p>
 */
public record SubmitOrderResponse(
        String orderId,
        OrderStatus status) {}
