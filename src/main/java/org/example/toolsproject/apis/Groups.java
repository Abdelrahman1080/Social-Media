package org.example.toolsproject.apis;

import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.models.groups.GroupDTO;
import org.example.toolsproject.models.groups.GroupPostDTO;
import org.example.toolsproject.ejbs.groups.GroupBean;
import org.example.toolsproject.ejbs.groups.GroupMembershipBean;
import org.example.toolsproject.ejbs.groups.GroupPostBean;
import org.example.toolsproject.models.User.User;
import org.example.toolsproject.models.groups.Group;
import org.example.toolsproject.models.groups.GroupPost;
import org.example.toolsproject.models.groups.GroupMembership;

@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Groups {

    private static final String GROUP_NOT_FOUND = "Group not found";

    @EJB
    private GroupBean groupBean;

    @EJB
    private GroupMembershipBean membershipBean;

    @EJB
    private GroupPostBean groupPostBean;

    @Context
    private HttpServletRequest request;

    @POST
    public Response createGroup(GroupDTO groupDto) {
        User creator = getAuthenticatedUser();
        groupBean.createGroup(groupDto, creator);
        return Response.status(Response.Status.CREATED).entity("Group created").build();
    }

    @POST
    @Path("/{groupId}/promote/{userId}")
    public Response promoteUserToAdmin(@PathParam("groupId") Long groupId, @PathParam("userId") Long userId) {
        User currentUser = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        User targetUser = groupBean.findUserById(userId);
        if (targetUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User to promote not found").build();
        }

        String result = membershipBean.promoteToAdmin(currentUser, group, targetUser);
        if ("User promoted to group admin".equals(result)) {
            return Response.ok(result).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        }
    }

    @POST
    @Path("/{groupId}/remove/{userId}")
    public Response removeUserFromGroup(@PathParam("groupId") Long groupId, @PathParam("userId") Long userId) {
        User currentUser = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        if (!group.isAdmin(currentUser)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only group admins can remove users").build();
        }

        User targetUser = groupBean.findUserById(userId);
        if (targetUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        GroupMembership membership = group.getMembershipForUser(targetUser);
        if (membership == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User is not a member of this group").build();
        }

        membershipBean.removeMembership(membership);
        return Response.ok("User removed from the group").build();
    }

    @POST
    @Path("/{groupId}/join")
    public Response requestToJoinGroup(@PathParam("groupId") Long groupId) {
        User user = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        String resultMessage = membershipBean.requestToJoinGroup(user, group);
        return Response.ok(resultMessage).build();
    }

    @POST
    @Path("/membership/{membershipId}/approve")
    public Response approveMembership(@PathParam("membershipId") Long membershipId) {
        membershipBean.approveMembership(membershipId);
        return Response.ok("Membership approved").build();
    }

    @POST
    @Path("/{groupId}/leave")
    public Response leaveGroup(@PathParam("groupId") Long groupId) {
        User user = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        membershipBean.leaveGroup(user, group);
        return Response.ok("Left group successfully").build();
    }

    @POST
    @Path("/{groupId}/posts")
    public Response postInGroup(@PathParam("groupId") Long groupId, GroupPostDTO postDto) {
        User user = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        try {
            GroupPost post = groupPostBean.createGroupPost(user, group, postDto.getContent());
            return Response.status(Response.Status.CREATED).entity(post).build();
        } catch (SecurityException se) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only members can post in this group").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid post content").build();
        }
    }
    @DELETE
    @Path("/{groupId}/delete")
    public Response deleteGroup(@PathParam("groupId") Long groupId) {
        User currentUser = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        if (!group.getCreator().equals(currentUser)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the group creator can delete the group").build();
        }

        groupBean.deleteGroup(group);
        return Response.ok("Group deleted successfully").build();
    }

    @DELETE
    @Path("/{groupId}/posts/{postId}")
    public Response deleteGroupPost(@PathParam("groupId") Long groupId, @PathParam("postId") Long postId) {
        User currentUser = getAuthenticatedUser();
        Group group = groupBean.findGroupById(groupId);

        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(GROUP_NOT_FOUND).build();
        }

        GroupPost post = groupPostBean.findGroupPostById(postId);
        if (post == null || !post.getGroup().getId().equals(groupId)) {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found in this group").build();
        }

        boolean isOwner = post.getUser().equals(currentUser);
        boolean isAdmin = group.isAdmin(currentUser);

        if (!isOwner && !isAdmin) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only the post owner or group admins can delete this post").build();
        }

        groupPostBean.deleteGroupPost(post);
        return Response.ok("Post deleted successfully").build();
    }


    private User getAuthenticatedUser() {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new WebApplicationException("User not authenticated", Response.Status.UNAUTHORIZED);
        }
        return (User) session.getAttribute("user");
    }
}
