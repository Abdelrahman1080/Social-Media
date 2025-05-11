package org.example.toolsproject.apis;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.ejbs.CommentLikeService;
import org.example.toolsproject.models.Post.DTOs.CommentDTO;

import java.util.ArrayList;
import java.util.List;

@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class CommentAPI {
    @Inject
    private CommentLikeService commentLikeService;
    @Context
    HttpServletRequest request;


    @POST
    @Path("{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CommentDTO createComment(@Context HttpServletRequest request,@PathParam("postId") int postId, CommentDTO commentDTO) {
        HttpSession session = request.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        try {
            CommentDTO createdComment = commentLikeService.createComment(postId, userId, commentDTO.getContent());
            return createdComment;
        }  catch (Exception e) {
            throw new WebApplicationException("Failed to create comment: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @GET
    @Path("{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CommentDTO> getCommentsByPostId(@PathParam("postId") int postId) {

        try {
            List<CommentDTO> comments = commentLikeService.getCommentsByPostId(postId);
            return comments != null ? comments : new ArrayList<>();
        } catch (Exception e) {
            throw new WebApplicationException("Failed to retrieve comments: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }





    @DELETE
    @Path("{postId}/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteComment(@Context HttpServletRequest request,@PathParam("postId") int postId, @PathParam("commentId") int commentId) {
        HttpSession session = request.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        try {
            commentLikeService.deleteComment(postId, commentId, userId);
        }  catch (Exception e) {
            throw new WebApplicationException("Failed to delete comment: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
