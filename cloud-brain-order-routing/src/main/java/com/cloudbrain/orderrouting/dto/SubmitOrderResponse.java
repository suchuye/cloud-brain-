package com.cloudbrain.orderrouting.dto;

import com.cloudbrain.shared.enums.OrderStatus;

public record SubmitOrderResponse(
        String orderId,
        OrderStatus status) {}
