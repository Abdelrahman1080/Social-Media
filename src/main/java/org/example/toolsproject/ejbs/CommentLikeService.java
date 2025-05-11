package org.example.toolsproject.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.toolsproject.models.Post.Comment;
import org.example.toolsproject.models.Post.DTOs.CommentDTO;
import org.example.toolsproject.models.Post.Like;
import org.example.toolsproject.models.Post.Post;

import java.util.List;
import java.util.stream.Collectors;
@Stateless
public class CommentLikeService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Comment CRUD
    public CommentDTO createComment(int postId, int userId, String content) {
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found");
        }
        Comment comment = new Comment(content, post);
        comment.setCommenterid(userId); // Assuming Comment has a userId field
        em.persist(comment);
        em.flush();

        // Notify the post owner
        int postOwnerId = post.getUser().getId();
        if (postOwnerId != userId) { // Don't notify if the user is commenting on their own post
            notifyUser(postOwnerId, userId, "commented on", postId);
        }

        return new CommentDTO(comment.getId(), comment.getContent());
    }

    public List<CommentDTO> getCommentsByPostId(int postId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList()
                .stream()
                .map(c -> new CommentDTO(c.getId(), c.getContent()))
                .collect(Collectors.toList());
    }

    public CommentDTO updateComment(int postId, int commentId, int userId, String content) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " not found");
        }
        if (comment.getPost().getId() != postId) {
            throw new IllegalArgumentException("Comment does not belong to post with ID " + postId);
        }
        if (comment.getCommenterid() != userId) {
            throw new IllegalArgumentException("User not authorized to update this comment");
        }
        comment.setContent(content);
        em.merge(comment);
        return new CommentDTO(comment.getId(), comment.getContent());
    }

    public void deleteComment(int postId, int commentId, int userId) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " not found");
        }
        if (comment.getPost().getId() != postId) {
            throw new IllegalArgumentException("Comment does not belong to post with ID " + postId);
        }
        if (comment.getCommenterid() != userId) {
            throw new IllegalArgumentException("User not authorized to delete this comment");
        }
        em.remove(comment);
    }

    // Like CRUD
    public void createLike(int postId, int userId) {
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " not found");
        }
        // Check for duplicate like
        long existingLikes = em.createQuery(
                        "SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId AND l.Likerid = :userId", Long.class)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getSingleResult();
        if (existingLikes > 0) {
            throw new IllegalArgumentException("User has already liked this post");
        }
        Like like = new Like(post, userId);
        em.persist(like);
        em.flush();

        // Notify the post owner
        int postOwnerId = post.getUser().getId();
        if (postOwnerId != userId) { // Don't notify if the user is liking their own post
            notifyUser(postOwnerId, userId, "liked", postId);
        }
    }

    public List<Integer> getLikesByPostId(int postId) {
        return em.createQuery("SELECT l.Likerid FROM Like l WHERE l.post.id = :postId", Integer.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void deleteLike(int postId, int userId) {
        List<Like> likes = em.createQuery("SELECT l FROM Like l WHERE l.post.id = :postId AND l.Likerid = :userId", Like.class)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getResultList();
        if (likes.isEmpty()) {
            throw new IllegalArgumentException("Like not found for post ID " + postId + " and user ID " + userId);
        }
        em.remove(likes.get(0));
    }

    // Placeholder for notification (to be implemented in phase 2)
    private void notifyUser(int postOwnerId, int actorId, String action, int postId) {
        System.out.println("Notification: User " + postOwnerId + " should be notified that User " + actorId +
                " performed action '" + action + "' on post " + postId);
    }

}
