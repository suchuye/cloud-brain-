package com.cloudbrain.auth.interfaces.rest;

import com.cloudbrain.auth.application.AuthService;
import com.cloudbrain.auth.dto.LoginRequest;
import com.cloudbrain.auth.dto.LoginResponse;
import com.cloudbrain.auth.dto.VerifyCredentialRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-credential")
    public ResponseEntity<Void> verifyCredential(@RequestBody VerifyCredentialRequest request) {
        authService.verifyCredential(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
