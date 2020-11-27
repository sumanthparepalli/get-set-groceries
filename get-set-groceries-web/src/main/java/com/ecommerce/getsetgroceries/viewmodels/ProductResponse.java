package com.ecommerce.getsetgroceries.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ProductResponse {
    private long productId;
    private String productName,productDescription;
    private Set<String> categories;
}
