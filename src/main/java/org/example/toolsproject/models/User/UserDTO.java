package org.example.toolsproject.models.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    public int id;
    public String name;
    public String email;
    public String bio;
    public List<UserFriendDTO> friends;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.friends = user.getFriends().stream()
                .map(UserFriendDTO::new)
                .collect(Collectors.toList());
    }
}

