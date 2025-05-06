package org.example.toolsproject.models.groups;

public class GroupJoinRequestDTO {
    private Long groupId;
    private Long userId;

    public GroupJoinRequestDTO() {}

    public GroupJoinRequestDTO(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
