package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "order_details",uniqueConstraints = {
        @UniqueConstraint(name = "order_details_unique", columnNames = {"order_id","product_id","seller_id"})
})
//@IdClass(OrderDetailsId.class)
@Data
@NamedQuery(name = "find_by_id", query = "select o from OrderDetails o where o.product.id=:product_id and o.seller.id=:seller_id and o.order.id=:order_id")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @Id
    @Column(name = "order_id")
    private long orderId;
//    @Id
    @Column(name = "product_id")
    private long productId;
//    @Id
    @Column(name = "seller_id")
    private long sellerId;
    private long quantity;
    private double pricePerUnit;
    private double price;
    private boolean delivered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    public Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    public Seller seller;

    public OrderDetails() {
    }

    public OrderDetails(long quantity, double pricePerUnit, double price, boolean delivered, long orderId, long productId, long sellerId) {
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.price = price;
        this.delivered = delivered;
    }

    public double getPrice(OrderDetails orderDetails) {
        return orderDetails.getPrice();
    }
}
