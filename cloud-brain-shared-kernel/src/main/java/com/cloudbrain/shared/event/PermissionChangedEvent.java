package com.cloudbrain.shared.event;

/**
 * 权限变更事件。
 * 医生被停权或角色变更时发布，各消费端需即时失效本地缓存的令牌。
 */
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
