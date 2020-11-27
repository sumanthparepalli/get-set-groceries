package com.ecommerce.getsetgroceries.models;


import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "inventory")
@IdClass(InventoryID.class)
@Data
@Builder
@AllArgsConstructor
public class Inventory {

    @Id
    @Column(name = "seller_id")
    private long sellerId;
    @Id
    @Column(name = "product_id")
    private long productId;
    @Range(min = 0, message = "{range.quantity}")
    @NotNull(message = "{required.quantity}")
    private Long quantity;
    @Range(min = 0, message = "{range.price}")
    @NotNull(message = "{required.price}")
    private Double price;
    @Range(min = 0, max = 100, message = "{range.discount}")
    @NotNull(message = "{required.discount}")
    private Double discount;
    private Long demand;

//    @Id
    @ManyToOne
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Seller seller;

//    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    public Product product;

  public Inventory(long sellerId, long productId, long quantity, double price, double discount, long demand) {
    this.sellerId = sellerId;
    this.productId = productId;
    this.quantity = quantity;
    this.price = price;
    this.discount = discount;
    this.demand = demand;
  }

  public Inventory() {
    }

}
