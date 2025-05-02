package org.example.toolsproject.Services;



import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.toolsproject.ejbs.Groups.GroupBean;
import org.example.toolsproject.ejbs.Groups.GroupMembershipBean;

@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Groups {

    @EJB
    private GroupBean groupBean;

    @EJB
    private GroupMembershipBean membershipBean;

    @POST
    public Response createGroup(Object groupDto) {
        groupBean.createGroup(groupDto, null);
        return Response.status(Response.Status.CREATED).entity("Group created").build();
    }

    @POST
    @Path("/{groupId}/join")
    public Response requestToJoinGroup(@PathParam("groupId") Long groupId) {
        membershipBean.requestToJoinGroup(null, null);
        return Response.ok("Join request sent").build();
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
        membershipBean.leaveGroup(null, null);
        return Response.ok("Left group").build();
    }
}