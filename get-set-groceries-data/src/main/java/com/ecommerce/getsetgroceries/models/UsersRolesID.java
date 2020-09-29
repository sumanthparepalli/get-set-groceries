package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsersRolesID implements Serializable {
    private User user;
    private Role role;

    public UsersRolesID(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
