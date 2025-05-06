package org.example.toolsproject.ejbs.groups;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.toolsproject.models.User.User;
import org.example.toolsproject.models.groups.Group;
import org.example.toolsproject.models.groups.GroupMembership;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class GroupMembershipBean {

    @PersistenceContext
    private EntityManager em;

    private static final Logger logger = Logger.getLogger(GroupMembershipBean.class.getName());

    private static final String STATUS_APPROVED = "APPROVED";
    private static final String STATUS_PENDING = "PENDING";

    public void updateMembership(GroupMembership membership) {
        em.merge(membership);
    }

    public void removeMembership(GroupMembership membership) {
        if (membership != null) {
            em.remove(em.contains(membership) ? membership : em.merge(membership));
        }
    }

    public String requestToJoinGroup(User user, Group group) {
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(group, "Group cannot be null");

        TypedQuery<GroupMembership> query = em.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.user = :user AND gm.group = :group",
                GroupMembership.class);
        query.setParameter("user", user);
        query.setParameter("group", group);

        if (!query.getResultList().isEmpty()) {
            return "You have already requested or joined this group.";
        }

        GroupMembership membership = new GroupMembership();
        membership.setUser(user);
        membership.setGroup(group);
        membership.setRole("MEMBER");

        if (group.isOpen()) {
            membership.setStatus(STATUS_APPROVED);
        } else {
            membership.setStatus(STATUS_PENDING);
        }

        em.persist(membership);

        if (logger.isLoggable(Level.INFO)) {
            logger.info("User " + user.getId() + " requested to join group " + group.getId());
        }

        return group.isOpen()
                ? "You have joined the group."
                : "Join request sent and is pending approval.";
    }

    public void leaveGroup(User user, Group group) {
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(group, "Group cannot be null");

        TypedQuery<GroupMembership> query = em.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.user = :user AND gm.group = :group",
                GroupMembership.class);
        query.setParameter("user", user);
        query.setParameter("group", group);

        GroupMembership membership = query.getResultStream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Membership not found."));

        em.remove(em.contains(membership) ? membership : em.merge(membership));

        if (logger.isLoggable(Level.INFO)) {
            logger.info("User " + user.getId() + " left group " + group.getId());
        }
    }

    public void approveMembership(Long membershipId) {
        Objects.requireNonNull(membershipId, "Membership ID cannot be null");

        GroupMembership membership = em.find(GroupMembership.class, membershipId);
        if (membership == null) {
            throw new IllegalArgumentException("Membership not found.");
        }

        if (!STATUS_PENDING.equalsIgnoreCase(membership.getStatus())) {
            throw new IllegalStateException("Membership is not pending approval.");
        }

        membership.setStatus(STATUS_APPROVED);
        em.merge(membership);

        if (logger.isLoggable(Level.INFO)) {
            logger.info("Approved group membership with ID: " + membershipId);
        }
    }

    public String promoteToAdmin(User promoter, Group group, User targetUser) {
        if (promoter == null || group == null || targetUser == null) {
            throw new IllegalArgumentException("Promoter, group, and target user must not be null");
        }

        if (!group.isAdmin(promoter)) {
            return "Only group admins can promote users";
        }

        GroupMembership membership = group.getMembershipForUser(targetUser);
        if (membership == null || !STATUS_APPROVED.equalsIgnoreCase(membership.getStatus())) {
            return "User is not an approved group member";
        }

        membership.setRole("ADMIN");
        updateMembership(membership);

        return "User promoted to group admin";
    }
}
