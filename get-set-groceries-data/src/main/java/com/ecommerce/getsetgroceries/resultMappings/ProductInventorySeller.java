package com.ecommerce.getsetgroceries.resultMappings;


import lombok.Data;
import org.springframework.stereotype.Component;


/*
 *   select p.id as product_id, p.name, p.description, s.id as seller_id, s.seller_name, i.price, i.quantity, i.discount,
 *          i.demand from products p
 *          join inventory i on p.id = i.product_id
 *          join sellers s on i.seller_id = s.id
 *          where s.zipcode=:zipcode
 * @EntityResult(entityClass = ProductInventorySeller.class,
                        fields = {
                                @FieldResult(name = "product_id", column = "product_id"),
                                @FieldResult(name = "product_name", column = "name"),
                                @FieldResult(name = "product_description", column = "description"),
                                @FieldResult(name = "seller_id", column = "seller_id"),
                                @FieldResult(name = "seller_name", column = "seller_name"),
                                @FieldResult(name = "price", column = "price"),
                                @FieldResult(name = "quantity", column = "quantity"),
                                @FieldResult(name = "discount", column = "discount"),
                                @FieldResult(name = "demand", column = "demand")
                        }
                )
 * */
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
