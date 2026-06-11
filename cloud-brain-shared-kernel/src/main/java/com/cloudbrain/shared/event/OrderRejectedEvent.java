package com.cloudbrain.shared.event;

public class OrderRejectedEvent extends DomainEvent {
    private String orderId;
    private String reason;

    public OrderRejectedEvent() {}
    public OrderRejectedEvent(String orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }
}
