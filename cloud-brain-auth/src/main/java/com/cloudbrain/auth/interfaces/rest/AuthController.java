package com.cloudbrain.auth.interfaces.rest;

import com.cloudbrain.auth.application.AuthService;
import com.cloudbrain.auth.dto.LoginRequest;
import com.cloudbrain.auth.dto.LoginResponse;
import com.cloudbrain.auth.dto.VerifyCredentialRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证 REST 接口。
 * 三个端点：登录、二次验证、刷新令牌。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** POST /auth/login — 用户名密码登录，返回 JWT 令牌对 */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * POST /auth/verify-credential — 二次密码验证。
     * 开具处方前调用，Gateway 限流每分钟 3 次。
     */
    @PostMapping("/verify-credential")
    public ResponseEntity<Void> verifyCredential(@RequestBody VerifyCredentialRequest request) {
        authService.verifyCredential(request);
        return ResponseEntity.ok().build();
    }

    /** POST /auth/refresh — 用 Refresh Token 换新 Access Token */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
