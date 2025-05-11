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

import java.util.List;

@Path("/likes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class LikeAPI {
    @Inject
    private CommentLikeService commentLikeService;
    @Context
    HttpServletRequest request;
    @POST
    @Path("{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void createLike(@Context HttpServletRequest request,@PathParam("postId") int postId) {
        HttpSession session = request.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        try {
            commentLikeService.createLike(postId, userId);
        }   catch (Exception e) {
            throw new WebApplicationException("Failed to create like: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    @GET
    @Path("{postId}")
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
    @Path("{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteLike(@Context HttpServletRequest request,@PathParam("postId") int postId) {
        HttpSession session = request.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        try {
            commentLikeService.deleteLike(postId, userId);
        }  catch (Exception e) {
            throw new WebApplicationException("Failed to delete like: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

}
