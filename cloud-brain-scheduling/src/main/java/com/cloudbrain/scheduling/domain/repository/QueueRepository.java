package com.cloudbrain.scheduling.domain.repository;

import com.cloudbrain.scheduling.domain.entity.Queue;
import com.cloudbrain.scheduling.domain.vo.QueueStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, String> {
    List<Queue> findByDoctorIdAndStatusOrderByQueueNumberAsc(String doctorId, QueueStatus status);
    Optional<Queue> findTopByDoctorIdAndStatusOrderByQueueNumberAsc(String doctorId, QueueStatus status);
    List<Queue> findByDoctorIdAndStatus(String doctorId, QueueStatus status);
}
