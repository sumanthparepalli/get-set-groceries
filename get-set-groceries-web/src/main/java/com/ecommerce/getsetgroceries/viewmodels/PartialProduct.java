package com.ecommerce.getsetgroceries.viewmodels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialProduct {
    private long product_id;
    private String product_name;
}
