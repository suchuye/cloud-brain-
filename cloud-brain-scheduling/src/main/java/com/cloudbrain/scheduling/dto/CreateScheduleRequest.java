package com.cloudbrain.scheduling.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateScheduleRequest(
        String doctorId,
        String doctorName,
        String departmentId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int maxPatients) {}
