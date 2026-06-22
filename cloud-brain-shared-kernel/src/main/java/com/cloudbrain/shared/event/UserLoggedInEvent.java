package com.cloudbrain.shared.event;

/**
 * 用户登录事件，用于安全审计和跨服务偏好初始化。
 */
public class UserLoggedInEvent extends DomainEvent {
    private String userId;
    private String role;

    public UserLoggedInEvent() {}
    public UserLoggedInEvent(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getRole() { return role; }
}
