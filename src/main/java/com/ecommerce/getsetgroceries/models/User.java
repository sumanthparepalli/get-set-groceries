package com.ecommerce.getsetgroceries.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),@UniqueConstraint(columnNames = {"email"})}
)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String mobile;
    @Column(name = "isMerchant")
    private Boolean isMerchant;
    @Column
    private Boolean enabled;
    @Column
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @EqualsAndHashCode.Exclude Set<Role> roles;

    public User() {
    }

    public User(String username, String email, String password, String mobile, Boolean isMerchant, Boolean enabled, Date date) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.isMerchant = isMerchant;
        this.enabled = enabled;
        this.createdAt = date;
    }
}
