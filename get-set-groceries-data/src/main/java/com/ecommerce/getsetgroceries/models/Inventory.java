package com.ecommerce.getsetgroceries.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
@IdClass(InventoryID.class)
@Data
public class Inventory {

//  @Id
//  @Column(name = "seller_id")
//  private long sellerId;

  private long quantity;
  private double price;
  private double discount;
  private long demand;

  @Id
  @ManyToOne
  @JoinColumn(name = "seller_id")
  @EqualsAndHashCode.Exclude @ToString.Exclude
  public Seller seller;

  @Id
  @ManyToOne
  @JoinColumn(name = "product_id")
  @EqualsAndHashCode.Exclude @ToString.Exclude
  public Product product;

  public Inventory(Seller seller, Product product, long quantity, double price, double discount, long demand) {
    this.seller = seller;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
    this.discount = discount;
    this.demand = demand;
  }

  public Inventory() {
  }

}
