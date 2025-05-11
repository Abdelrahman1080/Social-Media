package org.example.toolsproject.apis;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.models.Post.Comment;
import org.example.toolsproject.models.Post.DTOs.CommentDTO;
import org.example.toolsproject.models.Post.DTOs.PostDTO;
import org.example.toolsproject.models.Post.Like;
import org.example.toolsproject.models.Post.Post;
import org.example.toolsproject.ejbs.PostService;

import java.util.List;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class PostResource {
    @Inject
    private PostService postService;

    @POST
    @Path("create-post")
    public String createPost(  PostDTO postDTO) {
        try {

            Post post = postService.createPost( postDTO.getuserid(), postDTO.getContent(), postDTO.getImageUrl(), postDTO.getLinkUrl());
            return "Post created";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @GET
    @Path("get/{userId}/posts")
    public List<PostDTO> getPostsByUserId(@PathParam("userId") int userId) {
        if (userId <= 0) {
            throw new WebApplicationException("userId must be a positive integer", Response.Status.BAD_REQUEST);
        }
        try {
            List<PostDTO> posts =   postService.getFeed(userId);
            return posts ;
        } catch (Exception e) {
            throw new WebApplicationException("Failed to retrieve posts: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("{postId}")
    public PostDTO updatePost(@PathParam("postId") int postId, @QueryParam("userId") int userId, PostDTO postDTO) {

        try {
            PostDTO updatedPost = postService.updatePost(postId, userId, postDTO.getContent(), postDTO.getImageUrl(), postDTO.getLinkUrl(), postDTO.getCommentCount(), postDTO.getLikeCount());
            if (updatedPost == null) {
                throw new WebApplicationException("Post not found or user not authorized", Response.Status.NOT_FOUND);
            }
            return updatedPost;
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        } catch (Exception e) {
            throw new WebApplicationException("Failed to update post: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("{postId}")
    public String updatePost(@PathParam("postId") int postId, @QueryParam("userId") int userId) {

        try {
            postService.deletePost(postId, userId);

            return " Post deleted Successfully";

        } catch (Exception e) {
            throw new WebApplicationException("Failed to update post: " + e.getMessage());
        }
    }

    @POST
    @Path("/{postId}/like")
    public Response likePost(@PathParam("postId") int postId, @QueryParam("userId") int userId) {

        Like like = postService.likePost(postId, userId);
        return Response.status(Response.Status.CREATED).entity(like).build();
    }

    @POST
    @Path("/{postId}/comment")
    public Response commentOnPost(@PathParam("postId") int postId, @QueryParam("userId") int userId, CommentDTO commentDTO) {

        Comment comment = postService.commentOnPost(postId, userId, commentDTO.getContent());
        return Response.status(Response.Status.CREATED).entity(comment).build();
    }
}

