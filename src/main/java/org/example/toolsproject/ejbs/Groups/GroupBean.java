package org.example.toolsproject.ejbs.Groups;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class GroupBean {

    @PersistenceContext
    private EntityManager em;

    public Object createGroup(Object group, Object creator) {
        System.out.println("Creating group...");
        return group;
    }

    public Object findGroupById(Long groupId) {
        System.out.println("Finding group by ID: " + groupId);
        return null;
    }
}