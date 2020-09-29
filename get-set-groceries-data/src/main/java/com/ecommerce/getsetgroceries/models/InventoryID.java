package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryID implements Serializable {
    private Seller seller;
    private Product product;

    public InventoryID(Seller seller, Product product) {
        this.seller = seller;
        this.product = product;
    }
}
