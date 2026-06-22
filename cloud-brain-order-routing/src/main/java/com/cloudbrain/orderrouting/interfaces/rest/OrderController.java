package com.cloudbrain.orderrouting.interfaces.rest;

import com.cloudbrain.orderrouting.application.OrderRoutingService;
import com.cloudbrain.orderrouting.dto.SubmitOrderRequest;
import com.cloudbrain.orderrouting.dto.SubmitOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing endpoints for medical order submission.
 * <p>Only users with the DOCTOR role are authorized to submit orders.</p>
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRoutingService orderRoutingService;

    public OrderController(OrderRoutingService orderRoutingService) {
        this.orderRoutingService = orderRoutingService;
    }

    /**
     * Submits a new medical order. Requires the DOCTOR role.
     * Returns the existing order if a duplicate (same idempotency key) is detected.
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<SubmitOrderResponse> submitOrder(@RequestBody SubmitOrderRequest request) {
        return ResponseEntity.ok(orderRoutingService.submitOrder(request));
    }
}
