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

/**
 * 开发环境数据初始化。
 * 首次启动时自动创建默认角色（Doctor/Patient/Admin）和测试医生账户 doctor1。
 * 已存在数据时跳过（幂等）。
 */
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
        // 幂等：已有角色数据则跳过
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
    }
}
