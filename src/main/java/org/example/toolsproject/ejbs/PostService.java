package org.example.toolsproject.ejbs;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.toolsproject.models.Post.DTOs.PostDTO;
import org.example.toolsproject.models.Post.Comment;
import org.example.toolsproject.models.Post.Like;
import org.example.toolsproject.models.Post.Post;
import org.example.toolsproject.models.User.User;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Cr e a t  e Post from main
    public Post createPost(int userId, String content, String imageUrl, String linkUrl) {
        User user = em.find(User.class, userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLinkUrl(linkUrl);
        post.setCommentcount(0);
        post.setLikecount(0);
        post.setCreatedAt(LocalDateTime.now().toString());
        em.persist(post);
        return post;
    }



    // Edit Post
    public PostDTO updatePost(int postId, int userId, String content, String imageUrl, String linkUrl,int CommentCount, int LikeCount) {
        if (postId <= 0 || userId <= 0) {
            throw new IllegalArgumentException("postId and userId must be positive integers");
        }
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found");
        }
        if (post.getUser().getId() != userId) {
            throw new IllegalArgumentException("User not authorized to update this post");
        }
        // Update fields
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setLinkUrl(linkUrl);
        post.setCommentcount(CommentCount);
        post.setLikecount(LikeCount);
        em.merge(post);
        // Return updated DTO
        return new PostDTO(post.getId(), post.getUser().getId(), post.getContent(), post.getImageUrl(), post.getLinkUrl(),
                post.getComments() != null ? post.getComments().size() : 0,
                post.getLikes() != null ? post.getLikes().size() : 0,post.getCreatedAt());
    }
    public List<PostDTO> getFeed(int userId) {
        // Example implementation: Fetch posts for the user (e.g., from friends)
        return em.createQuery(
                        "SELECT new org.example.toolsproject.models.Post.DTOs.PostDTO(p.id, p.user.id, p.content, p.imageUrl, p.linkUrl, " +
                                "SIZE(p.comments), SIZE(p.likes),p.createdAt) " +
                                "FROM Post p WHERE p.user.id = :userId", PostDTO.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // Delete Post
    public void deletePost(int postId, int userId) {
        Post post = em.find(Post.class, postId);
        if (post == null || post.getUser().getId()!=userId) {
            throw new IllegalArgumentException("Invalid post or unauthorized");
        }
        em.remove(post);
    }

    // Like Post
    public Like likePost(int postId, int userId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        if (post == null || user == null) throw new IllegalArgumentException("Invalid post or user");

        Query query = em.createQuery("SELECT l FROM Like l WHERE l.post.id = :postId AND l.Likerid = :userId");
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);
        List<Like> existingLikes = query.getResultList();
        if (!existingLikes.isEmpty()) throw new IllegalStateException("Post already liked");

        Like like = new Like();
        like.setPost(post);
        like.setLikerid(user.getId());

        em.persist(like);
        return like;
    }

    // Comment on Post
    public Comment commentOnPost(int postId, int userId, String content) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        if (post == null || user == null) throw new IllegalArgumentException("Invalid post or user");

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setCommenterid(user.getId());
        comment.setContent(content);

        em.persist(comment);
        return comment;
    }
}