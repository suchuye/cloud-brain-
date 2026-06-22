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
                        .uri("lb://cloud-brain-auth"))
                .route("outpatient", r -> r.path("/api/consultations/**", "/api/emr/**")
                        .uri("lb://cloud-brain-outpatient"))
                .route("imaging", r -> r.path("/api/imaging/**")
                        .uri("lb://cloud-brain-imaging"))
                .route("ai-assistant-ws", r -> r.path("/ws/ai-assistant/**")
                        .uri("lb://cloud-brain-ai-assistant"))
                .route("scheduling", r -> r.path("/api/scheduling/**")
                        .uri("lb://cloud-brain-scheduling"))
                .route("order-routing", r -> r.path("/api/orders/**")
                        .uri("lb://cloud-brain-order-routing"))
                .route("patient", r -> r.path("/api/patient/**")
                        .uri("lb://cloud-brain-patient"))
                .route("admin", r -> r.path("/api/admin/**")
                        .uri("lb://cloud-brain-admin"))
                .build();
    }
}
