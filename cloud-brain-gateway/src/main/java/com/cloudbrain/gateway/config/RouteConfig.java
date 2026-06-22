package com.cloudbrain.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth", r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))
                .route("outpatient", r -> r.path("/api/consultations/**", "/api/emr/**")
                        .uri("http://localhost:8082"))
                .route("imaging", r -> r.path("/api/imaging/**")
                        .uri("http://localhost:8083"))
                .route("ai-assistant-ws", r -> r.path("/ws/ai-assistant/**")
                        .uri("http://localhost:8084"))
                .route("scheduling", r -> r.path("/api/scheduling/**")
                        .uri("http://localhost:8085"))
                .route("order-routing", r -> r.path("/api/orders/**")
                        .uri("http://localhost:8086"))
                .route("patient", r -> r.path("/api/patient/**")
                        .uri("http://localhost:8087"))
                .route("admin", r -> r.path("/api/admin/**")
                        .uri("http://localhost:8088"))
                // ai-llm is an internal service, not exposed through gateway
                .build();
    }
}
