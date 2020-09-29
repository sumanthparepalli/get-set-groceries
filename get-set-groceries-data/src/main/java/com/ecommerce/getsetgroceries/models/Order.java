package com.ecommerce.getsetgroceries.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private double amount;
  private long paid;
  private long delivered;

  @ManyToOne
  @JoinColumn(name = "user_id")
  public @EqualsAndHashCode.Exclude User user;
  @ManyToOne
  @JoinColumn(name = "address_id")
  public @EqualsAndHashCode.Exclude UserAddress address;
  @OneToMany(mappedBy = "order")
  public @EqualsAndHashCode.Exclude   Set<OrderDetails> orderDetails;

  public Order() {
  }

  public Order(double amount, long paid, long delivered, User user, UserAddress address, Set<OrderDetails> orderDetails) {
    this.amount = amount;
    this.paid = paid;
    this.delivered = delivered;
    this.user = user;
    this.address = address;
    this.orderDetails = orderDetails;
  }
}
