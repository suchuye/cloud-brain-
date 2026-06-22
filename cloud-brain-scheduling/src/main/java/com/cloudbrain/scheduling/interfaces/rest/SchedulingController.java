package com.cloudbrain.scheduling.interfaces.rest;

import com.cloudbrain.scheduling.application.SchedulingService;
import com.cloudbrain.scheduling.domain.entity.Queue;
import com.cloudbrain.scheduling.domain.entity.Schedule;
import com.cloudbrain.scheduling.dto.AddToQueueRequest;
import com.cloudbrain.scheduling.dto.CreateScheduleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing schedule management and patient queue operation endpoints.
 */
@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    // === 排班 ===

    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@RequestBody CreateScheduleRequest req) {
        return ResponseEntity.ok(schedulingService.createSchedule(req));
    }

    @GetMapping("/schedules/{doctorId}")
    public ResponseEntity<List<Schedule>> getSchedules(
            @PathVariable String doctorId,
            @RequestParam java.time.LocalDate date) {
        return ResponseEntity.ok(schedulingService.getDoctorSchedules(doctorId, date));
    }

    // === 队列 ===

    @PostMapping("/queue")
    public ResponseEntity<Queue> addToQueue(@RequestBody AddToQueueRequest req) {
        return ResponseEntity.ok(schedulingService.addToQueue(req));
    }

    @PostMapping("/queue/{id}/check-in")
    public ResponseEntity<Queue> checkIn(@PathVariable String id) {
        return ResponseEntity.ok(schedulingService.checkIn(id));
    }

    @PostMapping("/queue/call-next")
    public ResponseEntity<Queue> callNext(@RequestParam String doctorId) {
        return ResponseEntity.ok(schedulingService.callNext(doctorId));
    }

    @PostMapping("/queue/{id}/complete")
    public ResponseEntity<Void> complete(@PathVariable String id) {
        schedulingService.completeConsultation(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/queue/{id}/pass")
    public ResponseEntity<Void> pass(@PathVariable String id) {
        schedulingService.passPatient(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/queue/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        schedulingService.cancel(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/queue/{doctorId}")
    public ResponseEntity<List<Queue>> getQueue(@PathVariable String doctorId) {
        return ResponseEntity.ok(schedulingService.getDoctorQueue(doctorId));
    }
}
