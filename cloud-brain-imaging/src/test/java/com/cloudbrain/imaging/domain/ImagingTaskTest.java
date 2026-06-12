package com.cloudbrain.imaging.domain;

import com.cloudbrain.imaging.domain.entity.ImagingTask;
import com.cloudbrain.imaging.domain.vo.ImagingTaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagingTaskTest {

    private ImagingTask task;

    @BeforeEach
    void setUp() {
        task = new ImagingTask("c-1", "o-1", "CT", "HEAD",
                "/data/ct.dcm", "abc123");
    }

    @Test
    void shouldCreateWithReceivedStatus() {
        assertEquals(ImagingTaskStatus.RECEIVED, task.getStatus());
    }

    @Test
    void shouldProgressThroughPipeline() {
        task.startPreprocessing();
        assertEquals(ImagingTaskStatus.PREPROCESSING, task.getStatus());

        task.startInference();
        assertEquals(ImagingTaskStatus.INFERRING, task.getStatus());

        task.complete();
        assertEquals(ImagingTaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void shouldNotSkipPreprocessing() {
        assertThrows(IllegalStateException.class, task::startInference);
    }

    @Test
    void shouldNotStartInferenceTwice() {
        task.startPreprocessing();
        task.startInference();
        assertThrows(IllegalStateException.class, task::startInference);
    }

    @Test
    void shouldFailWithMessage() {
        task.fail("DICOM 文件损坏");
        assertEquals(ImagingTaskStatus.FAILED, task.getStatus());
        assertEquals("DICOM 文件损坏", task.getErrorMessage());
    }

    @Test
    void shouldStoreFilePathAndMd5() {
        assertEquals("/data/ct.dcm", task.getFilePath());
        assertEquals("abc123", task.getFileMd5());
    }
}
