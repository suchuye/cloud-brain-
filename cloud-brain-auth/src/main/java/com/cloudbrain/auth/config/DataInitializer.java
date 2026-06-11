package com.cloudbrain.auth.config;

import com.cloudbrain.auth.domain.entity.Role;
import com.cloudbrain.auth.domain.entity.User;
import com.cloudbrain.shared.enums.UserRole;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(EntityManager em, PasswordEncoder passwordEncoder) {
        this.em = em;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if ((long) em.createQuery("SELECT COUNT(r) FROM Role r").getSingleResult() > 0) {
            return;
        }

        Role doctorRole = new Role(UserRole.DOCTOR, Set.of("READ_EMR", "WRITE_EMR", "ISSUE_ORDER", "PRESCRIBE"));
        Role patientRole = new Role(UserRole.PATIENT, Set.of("READ_SELF"));
        Role adminRole = new Role(UserRole.ADMIN, Set.of("MANAGE_SCHEDULE", "MANAGE_USERS"));
        em.persist(doctorRole);
        em.persist(patientRole);
        em.persist(adminRole);

        User doctor = new User("doctor1", passwordEncoder.encode("test123"), "张医生",
                Set.of(UserRole.DOCTOR), "DEPT-001");
        em.persist(doctor);

        System.out.println("=== Test user created: doctor1 / test123 ===");
    }
}
