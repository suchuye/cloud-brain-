package com.cloudbrain.orderrouting.domain;

import com.cloudbrain.orderrouting.domain.entity.MedicalOrder;
import com.cloudbrain.shared.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MedicalOrderTest {

    @Test
    void shouldAcceptFromPending() {
        MedicalOrder order = new MedicalOrder("key-1", "c-1", "PRESCRIPTION", "{}");
        assertEquals(OrderStatus.PENDING, order.getStatus());

        order.accept();
        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
    }

    @Test
    void shouldNotAcceptTwice() {
        MedicalOrder order = new MedicalOrder("key-1", "c-1", "IMAGING", "{}");
        order.accept();

        assertThrows(IllegalStateException.class, order::accept);
    }

    @Test
    void shouldRouteAfterAccepted() {
        MedicalOrder order = new MedicalOrder("key-1", "c-1", "IMAGING", "{}");
        order.accept();
        order.route();
        assertEquals(OrderStatus.ROUTED, order.getStatus());
    }

    @Test
    void shouldNotRouteBeforeAccepted() {
        MedicalOrder order = new MedicalOrder("key-1", "c-1", "PRESCRIPTION", "{}");

        assertThrows(IllegalStateException.class, order::route);
    }

    @Test
    void shouldCompleteAfterRouted() {
        MedicalOrder order = new MedicalOrder("key-1", "c-1", "IMAGING", "{}");
        order.accept();
        order.route();
        order.complete();
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test
    void shouldReject() {
        MedicalOrder order = new MedicalOrder("key-1", "c-1", "PRESCRIPTION", "{}");
        order.accept();
        order.reject("库存不足");
        assertEquals(OrderStatus.REJECTED, order.getStatus());
    }

    @Test
    void idempotencyKeyShouldMatch() {
        MedicalOrder order = new MedicalOrder("unique-key-123", "c-1", "LAB", "{}");
        assertEquals("unique-key-123", order.getIdempotencyKey());
    }
}
