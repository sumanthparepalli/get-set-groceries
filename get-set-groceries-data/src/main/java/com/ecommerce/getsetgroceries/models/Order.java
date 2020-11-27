package com.ecommerce.getsetgroceries.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "address_id")
    private long addressId;
    private double total;
    private boolean paid;
    private boolean delivered;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date orderDate;
    private double promotion;
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    public User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public UserAddress address;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    public @EqualsAndHashCode.Exclude
    @ToString.Exclude
    List<OrderDetails> orderDetails;

    @OneToOne(mappedBy = "order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public OrderPayment payment;

    public Order() {
    }

    public Order(long userId, long addressId, double amount, boolean paid, boolean delivered) {
        this.userId = userId;
        this.addressId = addressId;
        this.amount = amount;
        this.paid = paid;
        this.delivered = delivered;
    }
}
