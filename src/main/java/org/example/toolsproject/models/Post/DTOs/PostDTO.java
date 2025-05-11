package org.example.toolsproject.models.Post.DTOs;

// DTOs for JSON serialization
public class PostDTO {
    private int postid;
    private String content;
    private String imageUrl;
    private String linkUrl;
    private int userid;
    private int commentCount;
    private int likeCount;
    private String createdAt;
    // Constructors
    public PostDTO() {}
    public PostDTO(int id, int userid, String content, String imageUrl, String linkUrl, int commentCount, int likeCount,String createdAt) {
        this.postid = id;
        this.userid = userid;
        this.content = content;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;

    }
    public int getId() { return postid; }
    public void setId(int id) { this.postid = id; }
    public int getuserid() { return userid; }
    public void setUserId(int userId) { this.userid = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
