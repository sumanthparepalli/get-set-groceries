package com.ecommerce.getsetgroceries.viewmodels;

import com.ecommerce.getsetgroceries.models.Order;
import com.ecommerce.getsetgroceries.models.OrderDetails;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class CartModel {
    private Order order;
    private List<OrderDetails> orderDetails;

    public CartModel() {
    }

    public CartModel(Order order, List<OrderDetails> orderDetails) {
        this.order = order;
        this.orderDetails = orderDetails;
    }
}
