package org.example.toolsproject.models.Post;


import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    private int Likerid;

    public Like(Post post, int userId) {
        this.post = post;
        this.Likerid = userId;
    }

    public Like() {

    }


    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public int getLikerid() {
        return Likerid;
    }

    public void setLikerid(int likerid) {
        Likerid = likerid;
    }
}