package com.cloudbrain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyCredentialRequest(
        @NotBlank String userId,
        @NotBlank String password) {}
