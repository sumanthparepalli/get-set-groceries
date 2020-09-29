package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user_address")
@Data
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String doorNo;
    private String streetName;
    private String city;
    private String zipcode;
    private String state;
    private double latitude;
    private double longitude;
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    public User user;
    @OneToMany(mappedBy = "address")
    public @EqualsAndHashCode.Exclude Set<Order> orders;

    public UserAddress() {
    }

    public UserAddress(String doorNo, String streetName, String city, String zipcode, String state, double latitude, double longitude, User users) {
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
