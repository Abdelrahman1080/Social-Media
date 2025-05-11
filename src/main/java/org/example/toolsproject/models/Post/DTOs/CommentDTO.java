package org.example.toolsproject.models.Post.DTOs;

public class CommentDTO {
    private String content;
    private int id;
    public CommentDTO(int id, String content) {
        this.content = content;
        this.id = id;
    }
    public CommentDTO() {}
    public CommentDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
