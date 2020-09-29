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
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private String mobile;
    @Column(name = "isMerchant")
    private Boolean isMerchant;
    private Boolean enabled;
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public @EqualsAndHashCode.Exclude Set<Role> roles;
    @OneToMany(mappedBy = "user")
    public @EqualsAndHashCode.Exclude Set<UserAddress> addresses;
    @OneToOne(mappedBy = "user")
    public @EqualsAndHashCode.Exclude
    Seller seller;
    @OneToMany(mappedBy = "user")
    public @EqualsAndHashCode.Exclude Set<Order> orders;
    @OneToMany(mappedBy = "user")
    public @EqualsAndHashCode.Exclude Set<CreditSchemeContri> credits;

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
