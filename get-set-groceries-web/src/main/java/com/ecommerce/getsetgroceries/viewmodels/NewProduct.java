package com.ecommerce.getsetgroceries.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class NewProduct {
    private Long product_id;
    private String product_name;
    private String product_description;
    private Long quantity;
    private Double discount;
    private Double price;
    private Set<String> categories;
    private List<MultipartFile> images;

    public NewProduct(Long product_id, String product_name, String product_description, Long quantity, Double discount, Double price, Set<String> categories) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.quantity = quantity;
        this.discount = discount;
        this.price = price;
        this.categories = categories;
    }
}
