package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDetailsId implements Serializable {
    private Order order;
    private Product product;
    private Seller seller;

    public OrderDetailsId(Order order, Product product, Seller seller) {
        this.order = order;
        this.product = product;
        this.seller = seller;
    }
}
