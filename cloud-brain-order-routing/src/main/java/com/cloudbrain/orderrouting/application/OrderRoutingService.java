package com.cloudbrain.orderrouting.application;

import com.cloudbrain.orderrouting.domain.entity.MedicalOrder;
import com.cloudbrain.orderrouting.domain.repository.MedicalOrderRepository;
import com.cloudbrain.orderrouting.dto.SubmitOrderRequest;
import com.cloudbrain.orderrouting.dto.SubmitOrderResponse;
import com.cloudbrain.orderrouting.infrastructure.messaging.OrderEventPublisher;
import com.cloudbrain.shared.event.OrderIssuedEvent;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderRoutingService {

    private final MedicalOrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderRoutingService(MedicalOrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public SubmitOrderResponse submitOrder(SubmitOrderRequest request) {
        // Idempotency check
        return orderRepository.findByIdempotencyKey(request.idempotencyKey())
                .map(existing -> new SubmitOrderResponse(existing.getId(), existing.getStatus()))
                .orElseGet(() -> {
                    MedicalOrder order = new MedicalOrder(
                            request.idempotencyKey(),
                            request.consultationId(),
                            request.orderType(),
                            request.orderDetails());
                    order.accept();
                    orderRepository.save(order);

                    // Insert outbox event in same transaction
                    eventPublisher.publish(new OrderIssuedEvent(
                            order.getId(),
                            order.getConsultationId(),
                            order.getOrderType(),
                            Map.of("details", order.getOrderDetails())));

                    return new SubmitOrderResponse(order.getId(), order.getStatus());
                });
    }
}
