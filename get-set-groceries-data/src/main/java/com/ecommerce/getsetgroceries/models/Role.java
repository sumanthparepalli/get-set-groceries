package com.ecommerce.getsetgroceries.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "roles"
)
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    @EqualsAndHashCode.Exclude Set<User> users;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
