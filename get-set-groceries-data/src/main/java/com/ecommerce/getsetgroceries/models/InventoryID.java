package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class InventoryID implements Serializable {

    private long sellerId;
    private long productId;

    public InventoryID() {
    }

    public InventoryID(long sellerId, long productId) {
        this.sellerId = sellerId;
        this.productId = productId;
    }
}
