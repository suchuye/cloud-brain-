package com.cloudbrain.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String userId,
        String name,
        String role) {}
