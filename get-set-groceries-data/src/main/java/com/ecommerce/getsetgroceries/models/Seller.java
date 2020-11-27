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
@Table(name = "sellers")
@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "seller_name",unique = true)
    @NotBlank
    private String sellerName;
    @Column(name = "door_no")
    @NotBlank
    private String doorNo;
    @Column(name = "street_name")
    @NotBlank
    private String streetName;
    @Column(name = "city")
    @NotBlank
    private String city;
//    @NumberFormat(pattern = "[0-9]{6}")
    @Column(name = "zipcode")
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
