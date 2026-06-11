package com.cloudbrain.scheduling.application;

import com.cloudbrain.scheduling.domain.entity.Queue;
import com.cloudbrain.scheduling.domain.entity.Schedule;
import com.cloudbrain.scheduling.domain.repository.QueueRepository;
import com.cloudbrain.scheduling.domain.repository.ScheduleRepository;
import com.cloudbrain.scheduling.dto.*;
import com.cloudbrain.scheduling.infrastructure.messaging.SchedulingEventPublisher;
import com.cloudbrain.shared.event.QueueUpdatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchedulingService {

    private final ScheduleRepository scheduleRepository;
    private final QueueRepository queueRepository;
    private final SchedulingEventPublisher eventPublisher;

    public SchedulingService(ScheduleRepository scheduleRepository,
                             QueueRepository queueRepository,
                             SchedulingEventPublisher eventPublisher) {
        this.scheduleRepository = scheduleRepository;
        this.queueRepository = queueRepository;
        this.eventPublisher = eventPublisher;
    }

    // ===== 排班 =====

    public Schedule createSchedule(CreateScheduleRequest req) {
        Schedule schedule = new Schedule(req.doctorId(), req.doctorName(), req.departmentId(),
                req.date(), req.startTime(), req.endTime(), req.maxPatients());
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getDoctorSchedules(String doctorId, java.time.LocalDate date) {
        return scheduleRepository.findByDoctorIdAndDate(doctorId, date);
    }

    // ===== 队列 =====

    @Transactional
    public Queue addToQueue(AddToQueueRequest req) {
        // 分配序号
        List<Queue> existing = queueRepository
                .findByDoctorIdAndStatusOrderByQueueNumberAsc(req.doctorId(), com.cloudbrain.scheduling.domain.vo.QueueStatus.WAITING);
        int nextNumber = existing.size() + 1;

        Queue queue = new Queue(req.doctorId(), req.patientId(), req.patientName(), nextNumber,
                req.source() != null ? req.source() : "ONLINE");
        return queueRepository.save(queue);
    }

    @Transactional
    public Queue checkIn(String queueId) {
        Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new RuntimeException("Queue not found"));
        queue.checkIn();
        queueRepository.save(queue);
        publishQueueEvent(queue, "CHECK_IN");
        return queue;
    }

    @Transactional
    public Queue callNext(String doctorId) {
        Queue next = queueRepository.findTopByDoctorIdAndStatusOrderByQueueNumberAsc(
                        doctorId, com.cloudbrain.scheduling.domain.vo.QueueStatus.IN_QUEUE)
                .orElseThrow(() -> new RuntimeException("No patient in queue"));

        next.call();
        queueRepository.save(next);
        publishQueueEvent(next, "CALL");
        return next;
    }

    @Transactional
    public void completeConsultation(String queueId) {
        Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new RuntimeException("Queue not found"));
        queue.complete();
        queueRepository.save(queue);
        publishQueueEvent(queue, "COMPLETE");
    }

    @Transactional
    public void passPatient(String queueId) {
        Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new RuntimeException("Queue not found"));
        queue.pass();
        queueRepository.save(queue);
        publishQueueEvent(queue, "PASS");
    }

    @Transactional
    public void cancel(String queueId) {
        Queue queue = queueRepository.findById(queueId).orElseThrow(() -> new RuntimeException("Queue not found"));
        queue.cancel();
        queueRepository.save(queue);
        publishQueueEvent(queue, "CANCEL");
    }

    public List<Queue> getDoctorQueue(String doctorId) {
        return queueRepository.findByDoctorIdAndStatusOrderByQueueNumberAsc(
                doctorId, com.cloudbrain.scheduling.domain.vo.QueueStatus.IN_QUEUE);
    }

    private void publishQueueEvent(Queue queue, String changeType) {
        eventPublisher.publish(new QueueUpdatedEvent(queue.getId(), queue.getDoctorId(), changeType));
    }
}
