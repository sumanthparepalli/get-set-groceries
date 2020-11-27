package com.ecommerce.getsetgroceries.models;

import lombok.Data;

@Data
public class CartReq {
    public long productId, sellerId;
    int quantity;
}
