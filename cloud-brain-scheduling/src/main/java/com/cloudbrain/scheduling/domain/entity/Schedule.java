package com.cloudbrain.scheduling.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Doctor schedule entity defining available time slots and patient capacity for a given day.
 */
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String doctorId;
    private String doctorName;
    private String departmentId;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private int maxPatients;       // 该时段最大接诊数
    private int currentPatients;   // 当前已挂号数

    private boolean active = true;

    public Schedule() {}

    public Schedule(String doctorId, String doctorName, String departmentId,
                    LocalDate date, LocalTime startTime, LocalTime endTime, int maxPatients) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.departmentId = departmentId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxPatients = maxPatients;
    }

    /**
     * Returns true if the schedule still has remaining patient capacity.
     */
    public boolean hasCapacity() { return currentPatients < maxPatients; }

    /**
     * Increments the patient count by one. Throws IllegalStateException if at capacity.
     */
    public void incrementPatients() {
        if (!hasCapacity()) throw new IllegalStateException("No capacity");
        this.currentPatients++;
    }

    public String getId() { return id; }
    public String getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getDepartmentId() { return departmentId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public int getMaxPatients() { return maxPatients; }
    public int getCurrentPatients() { return currentPatients; }
    public boolean isActive() { return active; }
}
