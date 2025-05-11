package org.example.toolsproject.models.Post;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private int commenterid;

    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    public Comment() {

    }

    public int getCommenterid() {
        return commenterid;
    }

    public void setCommenterid(int commenterid) {
        this.commenterid = commenterid;
    }

    @Column(length = 500)
    private String content;



    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

}