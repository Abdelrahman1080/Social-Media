package org.example.toolsproject.apis;


import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.example.toolsproject.ejbs.UserService;
import org.example.toolsproject.models.User.*;

import java.util.List;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
@DeclareRoles({"Admin", "User"})
public class Users {

    @EJB
    UserService userService;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/add")
    public String addUser(User user){
        return userService.addUser(user);
    }

    @POST
    @Path("/add-admin")
    @RolesAllowed("Admin")
    public String addAdmin(AdminUser admin){
        return userService.addAdminUser(admin);
    }

    @GET
    @Path("getUser/{id}")
    public UserDTO getUser(@PathParam("id") int id) {
        return userService.getUser(id);
    }

    @GET
    @Path("getAllUsers")
    @RolesAllowed("Admin")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @POST
    @Path("signin")
    public String signin(@QueryParam("email") String email, @QueryParam("password") String password) {
        User user = userService.signin(email, password);
        if(user == null) return "Invalid Credentials";

        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", (user instanceof AdminUser) ? "Admin" : "User");
        return  "User Successfully Logged In";
    }

    @PUT
    @Path("update/{id}")
    @RolesAllowed("Admin")
    public String updateUser(@PathParam("id") int id,@QueryParam("name") String name,@QueryParam("email") String email,@QueryParam("password") String password,@QueryParam("bio") String bio) {
        return userService.updateUser(id, name, email, password, bio);
    }

    @POST
    @Path("sendRequest")
    public String sendFriendRequest(@QueryParam("sender") int senderId, @QueryParam("receiver") int receiverId) {
        return userService.sendFriendRequest(senderId, receiverId);
    }

    @PUT
    @Path("accept/{id}")
    public String acceptFriendRequest(@PathParam("id") int id) {
        return userService.acceptFriendRequest(id);
    }

    @PUT
    @Path("reject/{id}")
    public String rejectFriendRequest(@PathParam("id") int id) {
        return userService.rejectFriendRequest(id);
    }

    @GET
    @Path("getRequests")
    public List<FriendRequest> getRequests(@QueryParam("id") int id) {
        return userService.getRequests(id);
    }

    @GET
    @Path("getUserFriends/{id}")
    public List<UserFriendDTO> getUserFriends(@PathParam("id") int id) {
        return userService.getUserFriends(id);
    }
}
