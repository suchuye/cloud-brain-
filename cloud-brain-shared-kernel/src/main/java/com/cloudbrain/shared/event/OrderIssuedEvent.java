package com.cloudbrain.shared.event;

import java.util.Map;

public class OrderIssuedEvent extends DomainEvent {
    private String orderId;
    private String consultationId;
    private String orderType; // PRESCRIPTION, IMAGING, LAB
    private Map<String, Object> orderDetails;

    public OrderIssuedEvent() {}
    public OrderIssuedEvent(String orderId, String consultationId, String orderType, Map<String, Object> orderDetails) {
        this.orderId = orderId;
        this.consultationId = consultationId;
        this.orderType = orderType;
        this.orderDetails = orderDetails;
    }

    public String getOrderId() { return orderId; }
    public String getConsultationId() { return consultationId; }
    public String getOrderType() { return orderType; }
    public Map<String, Object> getOrderDetails() { return orderDetails; }
}
