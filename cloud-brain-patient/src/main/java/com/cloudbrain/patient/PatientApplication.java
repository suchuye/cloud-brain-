package com.cloudbrain.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Spring Boot application entry point for the Patient module.
 * Handles patient-facing operations such as triage, booking, and report retrieval.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
public class PatientApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }
}
