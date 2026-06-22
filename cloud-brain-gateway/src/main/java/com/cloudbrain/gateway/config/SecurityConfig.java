package com.cloudbrain.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Security configuration for the API Gateway.
 * In production profiles, enforces JWT-based OAuth2 resource server authentication
 * on all routes except public ones (auth, WebSocket, actuator). In non-profiles,
 * all exchanges are permitted without authentication.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Builds the security filter chain with profile-aware authorization rules
     * and JWT resource server configuration.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, Environment env) {
        boolean isProd = Arrays.asList(env.getActiveProfiles()).contains("prod");

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        if (isProd) {
            http.authorizeExchange(ex -> ex
                            .pathMatchers("/auth/**", "/ws/ai-assistant/**", "/actuator/**").permitAll()
                            .anyExchange().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.jwtDecoder(jwtDecoder())));
        } else {
            http.authorizeExchange(ex -> ex.anyExchange().permitAll());
        }

        return http.build();
    }

    /**
     * Configures a reactive JWT decoder using HMAC-SHA256 with the secret
     * from the {@code JWT_SECRET} environment variable (or a default dev secret).
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String secret = System.getenv().getOrDefault("JWT_SECRET", "cloud-brain-platform-secret-key-min-256-bits!!");
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
