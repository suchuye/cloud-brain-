package com.cloudbrain.auth.domain.entity;

import com.cloudbrain.shared.enums.UserRole;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;

/**
 * 用户聚合根。
 * 存储登录凭据（bcrypt 哈希）、角色集合和所属科室。
 * 密码通过 {@link org.springframework.security.crypto.password.PasswordEncoder} 加密，永不存明文。
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    private String departmentId;

    private boolean enabled = true;

    private Instant createdAt = Instant.now();

    public User() {}

    public User(String username, String passwordHash, String name, Set<UserRole> roles, String departmentId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.roles = roles;
        this.departmentId = departmentId;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }
    public Set<UserRole> getRoles() { return roles; }
    public String getDepartmentId() { return departmentId; }
    public boolean isEnabled() { return enabled; }
    public Instant getCreatedAt() { return createdAt; }

    /** 停用账户（管理员操作或安全事件触发） */
    public void disable() { this.enabled = false; }

    /** 更新角色集合 */
    public void updateRoles(Set<UserRole> newRoles) { this.roles = newRoles; }
}
