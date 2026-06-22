package com.cloudbrain.shared.event;

/**
 * 医嘱驳回事件。
 * 下游因库存不足、禁忌症等原因驳回医嘱时发布，
 * 门诊工作台消费后向医生弹窗告警。
 */
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
