package com.cloudbrain.auth.domain.entity;

import com.cloudbrain.shared.enums.UserRole;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private UserRole name;

    @ElementCollection
    private Set<String> permissions;

    public Role() {}

    public Role(UserRole name, Set<String> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getId() { return id; }
    public UserRole getName() { return name; }
    public Set<String> getPermissions() { return permissions; }
}
