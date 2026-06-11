package com.cloudbrain.orderrouting.domain.entity;

import com.cloudbrain.shared.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "medical_order")
public class MedicalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String idempotencyKey; // consultation_id + content_hash

    @Column(nullable = false)
    private String consultationId;

    @Column(nullable = false)
    private String orderType; // PRESCRIPTION, IMAGING, LAB

    @Column(columnDefinition = "JSON")
    private String orderDetails;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private Instant createdAt = Instant.now();

    public MedicalOrder() {}

    public MedicalOrder(String idempotencyKey, String consultationId, String orderType, String orderDetails) {
        this.idempotencyKey = idempotencyKey;
        this.consultationId = consultationId;
        this.orderType = orderType;
        this.orderDetails = orderDetails;
    }

    public void accept() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order already processed");
        }
        this.status = OrderStatus.ACCEPTED;
    }

    public void route() {
        if (this.status != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Order must be accepted before routing");
        }
        this.status = OrderStatus.ROUTED;
    }

    public void complete() { this.status = OrderStatus.COMPLETED; }
    public void reject(String reason) { this.status = OrderStatus.REJECTED; }

    public String getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getConsultationId() { return consultationId; }
    public String getOrderType() { return orderType; }
    public String getOrderDetails() { return orderDetails; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
