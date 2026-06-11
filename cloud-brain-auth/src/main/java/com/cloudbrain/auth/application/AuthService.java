package com.cloudbrain.auth.application;

import com.cloudbrain.auth.domain.entity.User;
import com.cloudbrain.auth.domain.repository.UserRepository;
import com.cloudbrain.auth.dto.LoginRequest;
import com.cloudbrain.auth.dto.LoginResponse;
import com.cloudbrain.auth.dto.VerifyCredentialRequest;
import com.cloudbrain.auth.infrastructure.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void verifyCredential(VerifyCredentialRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credential");
        }
    }

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
