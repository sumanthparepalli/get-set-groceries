package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "user_address")
@Data
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String doorNo;
    @NotBlank
    private String streetName;
    @NotBlank
    private String city;
    @Size(max = 6, min = 6)
    private String zipcode;
    @NotBlank
    private String state;
    @DecimalMin(value = "-90")
    @DecimalMax(value = "+90")
    private double latitude;
    @DecimalMin(value = "-180")
    @DecimalMax(value = "+180")
    private double longitude;
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public User user;
    @OneToMany(mappedBy = "address")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<Order> orders;

    public UserAddress() {
    }

    public UserAddress(String doorNo, String streetName, String city, String zipcode, String state, double latitude, double longitude, User user) {
        this.doorNo = doorNo;
        this.streetName = streetName;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }
}
