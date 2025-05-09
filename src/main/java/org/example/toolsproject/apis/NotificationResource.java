package org.example.toolsproject.apis;

import org.example.toolsproject.models.Notifications.Notification;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;


@Path("/api/notifications")
@Stateless
public class NotificationResource {
    @PersistenceContext
    private EntityManager em;

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notification> getNotifications(@PathParam("userId") String userId) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.userId = :userId", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
