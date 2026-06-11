package com.cloudbrain.shared.event;

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
