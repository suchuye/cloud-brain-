package com.cloudbrain.shared.event;

public class OrderFulfilledEvent extends DomainEvent {
    private String orderId;
    private String fulfilledBy;

    public OrderFulfilledEvent() {}
    public OrderFulfilledEvent(String orderId, String fulfilledBy) {
        this.orderId = orderId;
        this.fulfilledBy = fulfilledBy;
    }

    public String getOrderId() { return orderId; }
    public String getFulfilledBy() { return fulfilledBy; }
}
