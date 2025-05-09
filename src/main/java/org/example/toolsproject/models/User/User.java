package org.example.toolsproject.models.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.example.toolsproject.models.Post.Post;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Table(name="Users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String role;

    @NotNull
    private String name;

    @NotNull
    @Column(unique=true)
    private String email;

    @NotNull
    @Column(unique=true)
    private String password;

    @NotNull
    private String bio;

    @Column(name = "user_type", insertable = false, updatable = false)
    private String userType;

    @OneToMany(mappedBy = "sender")
    private List<FriendRequest> sentRequests;

    @OneToMany(mappedBy = "receiver")
    private List<FriendRequest> receivedRequests;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User(String name, String email, String password, String bio) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.role = "USER";
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public void setName(String name) {
        this.name = name;
    }




    public String getEmail() {
        return email;
    }




    public void setEmail(String email) {
        this.email = email;
    }




    public String getPassword() {
        return password;
    }




    public void setPassword(String password) {
        this.password = password;
    }




    public String getBio() {
        return bio;
    }




    public void setBio(String bio) {
        this.bio = bio;
    };


    public String getUserType() {
        return userType;
    }

    public List<User> getFriends() {
        return friends;
    }
}
