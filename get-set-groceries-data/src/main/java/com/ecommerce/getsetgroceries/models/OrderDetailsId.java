package com.ecommerce.getsetgroceries.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDetailsId implements Serializable {
    private long orderId;
    private long productId;
    private long sellerId;

    public OrderDetailsId() {
    }

    public OrderDetailsId(long orderId, long productId, long sellerId) {
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
    }
}
