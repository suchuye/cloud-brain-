package com.cloudbrain.imaging.dto;

/**
 * Request payload for submitting an imaging task.
 * <p>Carries consultation/order references, imaging modality and body part,
 * the DICOM/NIfTI file path, and its MD5 checksum for integrity verification.</p>
 */
public record SubmitImageRequest(
        String consultationId,
        String orderId,
        String modality,
        String bodyPart,
        String filePath,
        String fileMd5) {}
