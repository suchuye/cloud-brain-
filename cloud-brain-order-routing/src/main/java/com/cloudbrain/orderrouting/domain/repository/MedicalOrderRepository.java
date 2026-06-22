package com.cloudbrain.orderrouting.domain.repository;

import com.cloudbrain.orderrouting.domain.entity.MedicalOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the MedicalOrder aggregate root.
 * <p>Provides idempotency-key-based lookup to support duplicate submission
 * detection.</p>
 */
public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, String> {
    Optional<MedicalOrder> findByIdempotencyKey(String idempotencyKey);
}
