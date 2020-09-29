package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
@IdClass(OrderDetailsId.class)
@Data
public class OrderDetails {
    //    @Id
//    @Column(name = "order_id")
//    private long orderId;
//    @Id
//    @Column(name = "product_id")
//    private long productId;
//    @Id
//    @Column(name = "seller_id")
//    private long sellerId;
    private long quantity;
    private double pricePerUnit;
    private double price;
    private boolean delivered;
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    public Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "seller_id")
    public Seller seller;

    public OrderDetails() {
    }

    public OrderDetails(long quantity, double pricePerUnit, double price, boolean delivered, Order order, Product product, Seller seller) {
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.price = price;
        this.delivered = delivered;
        this.order = order;
        this.product = product;
        this.seller = seller;
    }

}
