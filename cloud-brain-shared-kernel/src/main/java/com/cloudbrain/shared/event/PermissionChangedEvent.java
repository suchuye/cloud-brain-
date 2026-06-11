package com.cloudbrain.shared.event;

public class PermissionChangedEvent extends DomainEvent {
    private String userId;
    private String newRole;

    public PermissionChangedEvent() {}
    public PermissionChangedEvent(String userId, String newRole) {
        this.userId = userId;
        this.newRole = newRole;
    }

    public String getUserId() { return userId; }
    public String getNewRole() { return newRole; }
}
