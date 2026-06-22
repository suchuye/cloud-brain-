package com.cloudbrain.shared.event;

/**
 * 医嘱执行完成事件。
 * 下游（药房/检查科室）执行完医嘱后回传，
 * 门诊工作台消费后更新医嘱状态。
 */
public class OrderFulfilledEvent extends DomainEvent {
    private String orderId;
    /** 执行方标识（药房/检查科室 ID） */
    private String fulfilledBy;

    public OrderFulfilledEvent() {}
    public OrderFulfilledEvent(String orderId, String fulfilledBy) {
        this.orderId = orderId;
        this.fulfilledBy = fulfilledBy;
    }

    public String getOrderId() { return orderId; }
    public String getFulfilledBy() { return fulfilledBy; }
}
