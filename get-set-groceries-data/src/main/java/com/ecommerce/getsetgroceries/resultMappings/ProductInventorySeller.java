package com.ecommerce.getsetgroceries.resultMappings;


import lombok.Data;
import org.springframework.stereotype.Component;



@Component
@Data
public class ProductInventorySeller {
    public Long product_id, seller_id;
    public String product_name, product_description, seller_name;
    public int quantity, demand;
    public Double discount, price;

    public ProductInventorySeller(long product_id, String product_name, String product_description,
                                  long seller_id, String seller_name, double price, int quantity,
                                  double discount, int demand)
    {
        this.product_id = product_id;
        this.seller_id = seller_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.seller_name = seller_name;
        this.quantity = quantity;
        this.demand = demand;
        this.discount = discount;
        this.price = price;
    }

    public ProductInventorySeller() {

    }
}
