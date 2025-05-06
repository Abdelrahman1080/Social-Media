package org.example.toolsproject.ejbs.groups;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.toolsproject.models.User.User;
import org.example.toolsproject.models.groups.Group;
import org.example.toolsproject.models.groups.GroupPost;

import java.time.LocalDateTime;

@Stateless
public class GroupPostBean {

    @PersistenceContext
    private EntityManager em;

    public GroupPost createGroupPost(User user, Group group, String content) {
        if (user == null || group == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid post data.");
        }

        if (!group.isMember(user)) {
            throw new SecurityException("Only approved members can post in this group.");
        }

        GroupPost post = new GroupPost();
        post.setUser(user);
        post.setGroup(group);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());

        em.persist(post);
        return post;
    }

    public GroupPost findGroupPostById(Long postId) {
        return em.find(GroupPost.class, postId);
    }

    public void deleteGroupPost(GroupPost post) {
        if (em.contains(post)) {
            em.remove(post);
        } else {
            em.remove(em.merge(post));
        }
    }

}
