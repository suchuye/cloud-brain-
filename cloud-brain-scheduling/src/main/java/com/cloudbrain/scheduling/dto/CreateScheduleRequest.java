package com.cloudbrain.scheduling.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Request DTO for creating a new doctor schedule with time slot and patient capacity.
 */
public record CreateScheduleRequest(
        String doctorId,
        String doctorName,
        String departmentId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int maxPatients) {}
