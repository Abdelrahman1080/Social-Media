package org.example.toolsproject.models.User;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class AdminUser extends User {

    public AdminUser() {}

    public AdminUser(String name, String email, String password, String bio) {
        super( name,  email,  password,  bio);
    }

}
