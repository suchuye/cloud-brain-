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

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

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

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String secret = System.getenv().getOrDefault("JWT_SECRET", "cloud-brain-platform-secret-key-min-256-bits!!");
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
