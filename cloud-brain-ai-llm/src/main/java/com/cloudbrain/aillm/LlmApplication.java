package com.cloudbrain.aillm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Spring Boot application entry point for the AI LLM module.
 * Provides LLM-based inference capabilities (diagnosis, summarization) to other services.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LlmApplication {
    public static void main(String[] args) {
        SpringApplication.run(LlmApplication.class, args);
    }
}
