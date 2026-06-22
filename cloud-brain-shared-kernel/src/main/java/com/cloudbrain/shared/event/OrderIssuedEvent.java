package com.cloudbrain.shared.event;

import java.util.Map;

/**
 * 医嘱已发出事件。
 * 路由服务同步确认接收后在本地事务中持久化并发布，
 * 异步触发药房配药或检查科室预约。
 */
public class OrderIssuedEvent extends DomainEvent {
    private String orderId;
    private String consultationId;
    /** 医嘱类型：PRESCRIPTION（处方）/ IMAGING（影像检查）/ LAB（检验） */
    private String orderType;
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
