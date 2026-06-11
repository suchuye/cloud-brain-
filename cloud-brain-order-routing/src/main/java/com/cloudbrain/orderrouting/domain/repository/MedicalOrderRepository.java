package com.cloudbrain.orderrouting.domain.repository;

import com.cloudbrain.orderrouting.domain.entity.MedicalOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalOrderRepository extends JpaRepository<MedicalOrder, String> {
    Optional<MedicalOrder> findByIdempotencyKey(String idempotencyKey);
}
