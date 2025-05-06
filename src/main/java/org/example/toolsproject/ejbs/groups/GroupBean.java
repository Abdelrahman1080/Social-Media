package org.example.toolsproject.ejbs.groups;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.toolsproject.models.User.User;
import org.example.toolsproject.models.groups.Group;
import org.example.toolsproject.models.groups.GroupDTO;
import org.example.toolsproject.models.groups.GroupMembership;

@Stateless
public class GroupBean {

    @PersistenceContext
    private EntityManager em;

    public Group createGroup(GroupDTO groupDto, User creator) {
        Group group = new Group();
        group.setName(groupDto.getName());
        group.setDescription(groupDto.getDescription());
        group.setOpen(groupDto.isOpen());
        group.setCreator(creator);

        em.persist(group);

        GroupMembership adminMembership = new GroupMembership();
        adminMembership.setGroup(group);
        adminMembership.setUser(creator);
        adminMembership.setRole("ADMIN");
        adminMembership.setStatus("APPROVED");

        em.persist(adminMembership);

        return group;
    }

    public Group findGroupById(Long groupId) {
        return em.find(Group.class, groupId);
    }

    public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }
    public void deleteGroup(Group group) {
        if (em.contains(group)) {
            em.remove(group);
        } else {
            em.remove(em.merge(group));
        }
    }

    public void updateMembership(GroupMembership membership) {
        em.merge(membership);
    }
}
