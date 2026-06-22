package com.cloudbrain.auth.domain.repository;

import com.cloudbrain.auth.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    /** 按用户名查找（登录用） */
    Optional<User> findByUsername(String username);

    /** 检查用户名是否已存在（注册用） */
    boolean existsByUsername(String username);
}
