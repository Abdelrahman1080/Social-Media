package org.example.toolsproject.ejbs.Groups;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class GroupMembershipBean {

    @PersistenceContext
    private EntityManager em;

    public Object requestToJoinGroup(Object user, Object group) {
        System.out.println("User requested to join group.");
        return null;
    }

    public void leaveGroup(Object user, Object group) {
        System.out.println("User left the group.");
    }

    public void approveMembership(Long membershipId) {
        System.out.println("Approved group membership with ID: " + membershipId);
    }
}

