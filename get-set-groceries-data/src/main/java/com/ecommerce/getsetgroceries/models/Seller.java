package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sellers")
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String sellerName;
    private String doorNo;
    private String streetName;
    private String city;
    private String zipcode;
    private String state;
    private double latitude;
    private double longitude;

    @OneToOne
    @JoinColumn(name = "users_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public User user;

    @OneToMany(mappedBy = "seller")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<CreditSchemeReq> creditSchemes;

    @ManyToMany
    @JoinTable(
            name = "inventory",
            joinColumns = @JoinColumn(name ="seller_id"),
            inverseJoinColumns = @JoinColumn(name ="product_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Set<Product> products;

    public Seller() {
    }

    public Seller(String sellerName, String doorNo, String streetName, String city, String zipcode, String state, double latitude, double longitude, User user) {
        this.sellerName = sellerName;
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
