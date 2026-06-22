package com.cloudbrain.scheduling.domain.repository;

import com.cloudbrain.scheduling.domain.entity.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for the Schedule aggregate root.
 * Provides query methods by doctor/date and department/date for active schedules.
 */
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByDoctorIdAndDate(String doctorId, LocalDate date);
    List<Schedule> findByDepartmentIdAndDateAndActiveTrue(String departmentId, LocalDate date);
}
