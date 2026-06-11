package com.cloudbrain.scheduling.domain.repository;

import com.cloudbrain.scheduling.domain.entity.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByDoctorIdAndDate(String doctorId, LocalDate date);
    List<Schedule> findByDepartmentIdAndDateAndActiveTrue(String departmentId, LocalDate date);
}
