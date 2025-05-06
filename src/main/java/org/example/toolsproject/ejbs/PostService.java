package org.example.toolsproject.ejbs;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.toolsproject.models.Post.Comment;
import org.example.toolsproject.models.Post.Like;
import org.example.toolsproject.models.Post.Post;
import org.example.toolsproject.models.User.User;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostService {
    @PersistenceContext
    private EntityManager em;

    // Cr e a t e Post from main
    public Post createPost(Long userId, String content, String imageUrl, String linkUrl) {
        User user = em.find(User.class, userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLinkUrl(linkUrl);
        post.setCreatedAt(LocalDateTime.now());
        em.persist(post);
        return post;
    }

    // Retrieve Feed  have to do the frienship first
  /*  public List<Post> getFeed(int userId) {
        Query query = em.createQuery(
                "SELECT p FROM Post p WHERE p.user.id = :userId OR p.user.id IN " +
                        "(SELECT f.friendId FROM Friendship f WHERE f.userId = :userId AND f.status = 'ACCEPTED') " +
                        "ORDER BY p.createdAt DESC"
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }*/

    // Edit Post
    public Post updatePost(Long postId, int userId, String content, String imageUrl, String linkUrl) {
        Post post = em.find(Post.class, postId);
        if (post == null || post.getUser().getId()!=userId) {
            throw new IllegalArgumentException("Invalid post or unauthorized");
        }
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLinkUrl(linkUrl);
        em.merge(post);
        return post;
    }

    // Delete Post
    public void deletePost(Long postId, int userId) {
        Post post = em.find(Post.class, postId);
        if (post == null || post.getUser().getId()!=userId) {
            throw new IllegalArgumentException("Invalid post or unauthorized");
        }
        em.remove(post);
    }

    // Like Post
    public Like likePost(Long postId, Long userId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        if (post == null || user == null) throw new IllegalArgumentException("Invalid post or user");

        Query query = em.createQuery("SELECT l FROM Like l WHERE l.post.id = :postId AND l.user.id = :userId");
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);
        List<Like> existingLikes = query.getResultList();
        if (!existingLikes.isEmpty()) throw new IllegalStateException("Post already liked");

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        like.setCreatedAt(LocalDateTime.now());
        em.persist(like);
        return like;
    }

    // Comment on Post
    public Comment commentOnPost(Long postId, Long userId, String content) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        if (post == null || user == null) throw new IllegalArgumentException("Invalid post or user");

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        em.persist(comment);
        return comment;
    }
}