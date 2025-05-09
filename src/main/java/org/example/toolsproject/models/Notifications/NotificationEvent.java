package org.example.toolsproject.models.Notifications;

import java.io.Serializable;

public class NotificationEvent implements Serializable {
    private String eventType;

    private String userId;

    private String relatedUserId;

    private String postId;

    private String groupId;

    private String taskUpdates;

    public NotificationEvent() {}

    public NotificationEvent(String eventType, String userId, String relatedUserId, String postId, String groupId, String taskUpdates) {
        this.eventType = eventType;
        this.userId = userId;
        this.relatedUserId = relatedUserId;
        this.postId = postId;
        this.groupId = groupId;
        this.taskUpdates = taskUpdates;
    }

    // Getters and setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getRelatedUserId() { return relatedUserId; }
    public void setRelatedUserId(String relatedUserId) { this.relatedUserId = relatedUserId; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    public String getTaskUpdates() { return taskUpdates; }
    public void setTaskUpdates(String taskUpdates) { this.taskUpdates = taskUpdates; }
}
