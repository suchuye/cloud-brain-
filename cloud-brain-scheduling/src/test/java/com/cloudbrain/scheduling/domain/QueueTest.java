package com.cloudbrain.scheduling.domain;

import com.cloudbrain.scheduling.domain.entity.Queue;
import com.cloudbrain.scheduling.domain.vo.QueueStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    private Queue queue;

    @BeforeEach
    void setUp() {
        queue = new Queue("d-1", "p-1", "张三", 1, "ONLINE");
    }

    @Test
    void shouldCreateWithWaitingStatus() {
        assertEquals(QueueStatus.WAITING, queue.getStatus());
    }

    @Test
    void shouldCheckInFromWaiting() {
        queue.checkIn();
        assertEquals(QueueStatus.IN_QUEUE, queue.getStatus());
    }

    @Test
    void shouldNotCheckInTwice() {
        queue.checkIn();
        assertThrows(IllegalStateException.class, queue::checkIn);
    }

    @Test
    void shouldCallFromInQueue() {
        queue.checkIn();
        queue.call();
        assertEquals(QueueStatus.IN_CONSULTATION, queue.getStatus());
    }

    @Test
    void shouldNotCallFromWaiting() {
        assertThrows(IllegalStateException.class, queue::call);
    }

    @Test
    void shouldCompleteFromInConsultation() {
        queue.checkIn();
        queue.call();
        queue.complete();
        assertEquals(QueueStatus.COMPLETED, queue.getStatus());
    }

    @Test
    void shouldPassFromInConsultation() {
        queue.checkIn();
        queue.call();
        queue.pass();
        assertEquals(QueueStatus.PASSED, queue.getStatus());
    }

    @Test
    void shouldCancelFromWaiting() {
        queue.cancel();
        assertEquals(QueueStatus.CANCELLED, queue.getStatus());
    }

    @Test
    void shouldNotCancelCompleted() {
        queue.checkIn();
        queue.call();
        queue.complete();
        assertThrows(IllegalStateException.class, queue::cancel);
    }

    @Test
    void shouldTrackEvents() {
        assertTrue(queue.getEvents().isEmpty());
        queue.checkIn();   // no event
        queue.call();       // CALLED
        queue.complete();   // COMPLETED
        assertEquals(2, queue.getEvents().size());
    }
}
