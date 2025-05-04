package org.example.toolsproject.ejbs;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import org.example.toolsproject.Models.User.*;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;


    @POST
    @Path("/add")
    public String addUser(User user) {
        try{
            em.persist(user);
            return "User Persisted Successfully";
        }
        catch(Exception e){
            return "Error Persisting User";
        }
    }

    @POST
    @Path("/add-admin")
    public String addAdminUser(AdminUser admin) {
        try{
            em.persist(admin);
            return "Admin Persisted Successfully";
        }
        catch(Exception e){
            return "Error Persisting Admin";
        }
    }

    @GET
    @Path("getUser/{id}")
    public UserDTO getUser(@PathParam("id") int id) {
        try {
            User user = em.find(User.class, id);
            return new UserDTO(user);
        }
        catch(Exception e){
            return null;
        }
    }

    @GET
    @Path("getAllUsers")
    public List<UserDTO> getAllUsers() {
        String query = "select u FROM User u";
        TypedQuery<User> queryObject = em.createQuery(query, User.class);
        List<User>  users = queryObject.getResultList();

        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @POST
    @Path("signin")
    public User signin(@QueryParam("email") String email, @QueryParam("password") String password) {
        try{
            String query = "select u from User u where u.email=:email and u.password=:password";
            Query queryObject = em.createQuery(query);
            queryObject.setParameter("email", email);
            queryObject.setParameter("password", password);
            return (User) queryObject.getSingleResult();
        }
        catch(Exception e){
            return null;
        }
    }

    @PUT
    @Path("update/{id}")
    public String updateUser(@PathParam("id") int id,@QueryParam("name") String name,@QueryParam("email") String email,@QueryParam("password") String password,@QueryParam("bio") String bio) {
        try{
            String query = "select u from User u where u.id = :id";
            Query queryObject = em.createQuery(query);
            queryObject.setParameter("id", id);
            User user = (User) queryObject.getSingleResult();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setBio(bio);
            return "User Information Updated Successfully";
        } catch (Exception e) {
            return "Error Updating User Information";
        }
    }

    @POST
    @Path("sendRequest")
    public String sendFriendRequest(@QueryParam("sender") int senderId, @QueryParam("receiver") int receiverId) {
        try{
            User sender =  em.find(User.class, senderId);
            User receiver =  em.find(User.class, receiverId);

            if(sender.equals(receiver)){
                throw new Exception();
            }

            if(em.find(FriendRequest.class, senderId).getReceiver().equals(receiver)){
                throw new Exception();
            }

            if(sender.getFriends().contains(receiver)){
                throw new Exception();
            }

            FriendRequest request = new FriendRequest(sender, receiver);
            em.persist(request);

            return "Friend Request Sent";
        }
        catch(Exception e){
            return "Error Sending Friend Request";
        }
    }

    @PUT
    @Path("accept/{id}")
    public String acceptFriendRequest(@PathParam("id") int id) {
        try{
            FriendRequest request = em.find(FriendRequest.class, id);
            if(request != null && request.getStatus() == FriendRequest.Status.PENDING){
                request.setStatus(FriendRequest.Status.ACCEPTED);
                request.getSender().getFriends().add(request.getReceiver());
                request.getReceiver().getFriends().add(request.getSender());
                return "Friend Request Accepted";
            }
            throw new Exception();
        }
        catch(Exception e){
            return "Error Accepting Friend Request";
        }
    }

    @PUT
    @Path("reject/{id}")
    public String rejectFriendRequest(@PathParam("id") int id) {
        try{
            FriendRequest request = em.find(FriendRequest.class, id);
            if(request != null && request.getStatus() == FriendRequest.Status.PENDING){
                request.setStatus(FriendRequest.Status.REJECTED);
                return "Friend Request Rejected";
            }
            throw new Exception();
        }
        catch(Exception e){
            return "Error Rejecting Friend Request";
        }
    }

    @GET
    @Path("getRequests")
    public List<FriendRequest> getRequests(@QueryParam("id") int id) {
        try{
            String requestQuery = "SELECT fr FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status = :status";
            TypedQuery<FriendRequest> queryRequest = em.createQuery(requestQuery, FriendRequest.class);
            queryRequest.setParameter("userId", id);
            queryRequest.setParameter("status", FriendRequest.Status.PENDING);
            return queryRequest.getResultList();
        }
        catch(Exception e){
            return null;
        }
    }

    @GET
    @Path("getUserFriends/{id}")
    public List<UserFriendDTO> getUserFriends(@PathParam("id") int id) {
        try {
            User user = em.find(User.class, id);
            return user.getFriends().stream().map(UserFriendDTO::new).collect(Collectors.toList());
        }
        catch(Exception e){
            return null;
        }
    }
}
