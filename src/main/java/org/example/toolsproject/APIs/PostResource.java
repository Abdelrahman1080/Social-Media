package org.example.toolsproject.APIs;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.example.toolsproject.ejbs.PostEJBs.Comment;
import org.example.toolsproject.ejbs.PostEJBs.Like;
import org.example.toolsproject.ejbs.PostEJBs.Post;
import org.example.toolsproject.Services.PostService;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {
    @Inject
    private PostService postService;
    @Context
    private HttpServletRequest request;
    @POST
    public Response createPost(PostDTO postDTO) {

        Long userId = getAuthenticatedUserId();
        Post post = postService.createPost(userId, postDTO.getContent(), postDTO.getImageUrl(), postDTO.getLinkUrl());
        return Response.status(Response.Status.CREATED).entity(post).build();
    }

    //After creating friendship
   /* @GET
    public Response getFeed() {
        Long userId = getAuthenticatedUserId();
        List<Post> posts = postService.getFeed(userId);
        return Response.ok(posts).build();
    }*/

    @PUT
    @Path("/{postId}")
    public Response updatePost(@PathParam("postId") Long postId, PostDTO postDTO) {
        Long userId = getAuthenticatedUserId();
        Post updatedPost = postService.updatePost(postId, Math.toIntExact(userId), postDTO.getContent(), postDTO.getImageUrl(), postDTO.getLinkUrl());
        return Response.ok(updatedPost).build();
    }

    @DELETE
    @Path("/{postId}")
    public Response deletePost(@PathParam("postId") Long postId) {
        Long userId = getAuthenticatedUserId();
        postService.deletePost(postId, Math.toIntExact(userId));
        return Response.noContent().build();
    }

    @POST
    @Path("/{postId}/like")
    public Response likePost(@PathParam("postId") Long postId) {
        Long userId = getAuthenticatedUserId();
        Like like = postService.likePost(postId, userId);
        return Response.status(Response.Status.CREATED).entity(like).build();
    }

    @POST
    @Path("/{postId}/comment")
    public Response commentOnPost(@PathParam("postId") Long postId, CommentDTO commentDTO) {
        Long userId = getAuthenticatedUserId();
        Comment comment = postService.commentOnPost(postId, userId, commentDTO.getContent());
        return Response.status(Response.Status.CREATED).entity(comment).build();
    }

    private Long getAuthenticatedUserId() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new WebApplicationException("No session available", Response.Status.UNAUTHORIZED);
        }
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new WebApplicationException("User not authenticated", Response.Status.UNAUTHORIZED);
        }
        return userId;
    }
}

    // DTOs for JSON serialization
    class PostDTO {
        private String content;
        private String imageUrl;
        private String linkUrl;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getLinkUrl() { return linkUrl; }
        public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
    }

    class CommentDTO {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }



