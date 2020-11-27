package com.ecommerce.getsetgroceries.models;

import com.ecommerce.getsetgroceries.validation.Username;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}), @UniqueConstraint(columnNames = {"email"})}
)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true)
    @NotBlank
    @Username
    private String username;
    @NotBlank
    @Email(regexp = ".+@.+\\..+")
    @Column(name = "email", unique = true)
    private String email;
    @NotBlank
    @ToString.Exclude
    private String password;
    @NotBlank
    private String mobile;
    @Column(name = "is_merchant")
    private Boolean isMerchant;
    private Boolean enabled;
    @Column(name = "created_at")
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<Role> roles;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<UserAddress> addresses;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Seller seller;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public List<Order> orders;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public List<CreditSchemeContri> credits;

    public User() {
        if (roles == null) {
            roles = new HashSet<>();
        }
    }

    public User(String username, String email, String password, String mobile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.isMerchant = false;
        this.enabled = true;
        this.createdAt = new Date(System.currentTimeMillis());
        roles = new HashSet<>();
    }
}
