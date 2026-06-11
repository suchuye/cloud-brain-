package com.cloudbrain.imaging.dto;

public record SubmitImageRequest(
        String consultationId,
        String orderId,
        String modality,
        String bodyPart,
        String filePath,
        String fileMd5) {}
