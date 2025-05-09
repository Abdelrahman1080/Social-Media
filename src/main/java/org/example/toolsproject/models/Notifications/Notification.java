package org.example.toolsproject.models.Notifications;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String eventType;
    private String relatedUserId;
    private String taskUpdates;

    // Constructors, getters, and setters
    public Notification() {}

    public Notification(String userId, String eventType, String relatedUserId, String taskUpdates) {
        this.userId = userId;
        this.eventType = eventType;
        this.relatedUserId = relatedUserId;
        this.taskUpdates = taskUpdates;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getRelatedUserId() { return relatedUserId; }
    public void setRelatedUserId(String relatedUserId) { this.relatedUserId = relatedUserId; }
    public String getTaskUpdates() { return taskUpdates; }
    public void setTaskUpdates(String taskUpdates) { this.taskUpdates = taskUpdates; }
}
