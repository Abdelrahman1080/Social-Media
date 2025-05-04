package org.example.toolsproject.Models.User;

public class UserFriendDTO {
    public int id;
    public String name;
    public String email;

    public UserFriendDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}

