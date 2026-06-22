package com.cloudbrain.auth.application;

import com.cloudbrain.auth.domain.entity.User;
import com.cloudbrain.auth.domain.repository.UserRepository;
import com.cloudbrain.auth.dto.LoginRequest;
import com.cloudbrain.auth.dto.LoginResponse;
import com.cloudbrain.auth.dto.VerifyCredentialRequest;
import com.cloudbrain.auth.infrastructure.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证应用服务。
 * 处理登录、令牌刷新和二次密码验证。
 * 所有验证失败统一抛出 RuntimeException，由 Controller 层转为 403。
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 用户名密码登录。
     * 检查账户启用状态 → 验证密码 → 签发双令牌。
     */
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account disabled");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getName(),
                user.getRoles().iterator().next().name());
    }

    /**
     * 二次密码验证。
     * 开具处方等高风险操作前调用，Gateway 限流配合。
     */
    public void verifyCredential(VerifyCredentialRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credential");
        }
    }

    /**
     * Refresh Token 换取新 Access Token。
     * Refresh Token 验证通过后签发新 Access Token，原 Refresh Token 仍有效。
     */
    public LoginResponse refresh(String refreshToken) {
        String userId = jwtProvider.validateRefreshToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtProvider.generateAccessToken(user);
        return new LoginResponse(
                newAccessToken,
                refreshToken,
                user.getId(),
                user.getName(),
                user.getRoles().iterator().next().name());
    }
}
