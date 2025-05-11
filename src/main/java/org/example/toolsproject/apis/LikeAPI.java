package org.example.toolsproject.apis;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.ejbs.CommentLikeService;

import java.util.List;

@Path("/likes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class LikeAPI {
    @Inject
    private CommentLikeService commentLikeService;
    @POST
    @Path("{postId}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public void createLike(@PathParam("postId") int postId, @QueryParam("userId") int userId) {

        try {
            commentLikeService.createLike(postId, userId);
        }   catch (Exception e) {
            throw new WebApplicationException("Failed to create like: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @GET
    @Path("{postId}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    public int getLikesByPostId(@PathParam("postId") int postId) {

        try {
            List<Integer> userIds = commentLikeService.getLikesByPostId(postId);
            return userIds.size();
        } catch (Exception e) {
            throw new WebApplicationException("Failed to retrieve likes: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @DELETE
    @Path("{postId}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteLike(@PathParam("postId") int postId, @QueryParam("userId") int userId) {

        try {
            commentLikeService.deleteLike(postId, userId);
        }  catch (Exception e) {
            throw new WebApplicationException("Failed to delete like: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
