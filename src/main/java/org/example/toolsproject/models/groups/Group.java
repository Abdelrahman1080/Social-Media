package org.example.toolsproject.models.groups;

import jakarta.persistence.*;
import org.example.toolsproject.models.User.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMembership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupPost> posts = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { isOpen = open; }

    public List<GroupMembership> getMemberships() { return memberships; }
    public void setMemberships(List<GroupMembership> memberships) { this.memberships = memberships; }

    public List<GroupPost> getPosts() { return posts; }
    public void setPosts(List<GroupPost> posts) { this.posts = posts; }

    public boolean isAdmin(User user) {
        if (user == null) return false;
        return creator.equals(user) || memberships.stream()
                .anyMatch(m -> m.getUser().equals(user)
                        && m.getRole().equalsIgnoreCase("ADMIN")
                        && m.getStatus().equalsIgnoreCase("APPROVED"));
    }

    public boolean isMember(User user) {
        if (user == null) return false;
        return memberships.stream()
                .anyMatch(m -> m.getUser().equals(user)
                        && m.getStatus().equalsIgnoreCase("APPROVED"));
    }

    public GroupMembership getMembershipForUser(User user) {
        if (user == null) return null;
        return memberships.stream()
                .filter(m -> m.getUser().equals(user))
                .findFirst()
                .orElse(null);
    }


    public void addMembership(GroupMembership membership) {
        memberships.add(membership);
        membership.setGroup(this);
    }

    public void removeMembership(GroupMembership membership) {
        memberships.remove(membership);
        membership.setGroup(null);
    }
}
